package com.wayfarer.immersivemaps.logic;

import java.util.Map;
import java.util.Map.Entry;

import com.wayfarer.api.WayfarerRegistry;
import com.wayfarer.immersivemaps.config.ImmersiveMapsConfig;
import com.wayfarer.immersivemaps.util.ColorHelper;
import com.wayfarer.immersivemaps.util.IMapItemSavedData;
import com.wayfarer.immersivemaps.util.WaypointUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public class MapMarkerLogic {

    public static void onServerTick(ServerPlayer player) {
        if (player.tickCount % 20 != 0)
            return;

        boolean hasMap = checkHand(player, player.getMainHandItem()) || checkHand(player, player.getOffhandItem());

        if (!hasMap) {
            WayfarerRegistry.clearWaypoints(player);
        }
    }

    private static boolean checkHand(ServerPlayer player, ItemStack stack) {
        if (stack.is(Items.FILLED_MAP)) {
            MapId mapId = stack.get(DataComponents.MAP_ID);
            if (mapId != null) {
                MapItemSavedData data = player.level().getMapData(mapId);
                if (data != null) {
                    WayfarerRegistry.clearWaypoints(player);
                    syncMapMarkers(player, data);
                    return true;
                }
            }
        }
        return false;
    }

    private static void syncMapMarkers(ServerPlayer player, MapItemSavedData data) {
        double centerOffset = (double) (1 << data.scale);
        IMapItemSavedData accessor = (IMapItemSavedData) data;
        Map<String, BlockPos> accurate = accessor.markers_getAccuratePositions();
        Map<String, MapDecoration> decorations = accessor.markers_getDecorations();
        ServerLevel level = (ServerLevel) player.level();

        for (Entry<String, MapDecoration> entry : decorations.entrySet()) {
            MapDecoration decoration = entry.getValue();
            ResourceLocation typeId = decoration.type().value().assetId();
            String typePath = typeId.getPath();

            if (isSupported(typePath)) {
                boolean isBanner = typePath.contains("banner");
                if (isBanner && !ImmersiveMapsConfig.sendBannerWaypoints)
                    continue;
                if (!isBanner && !ImmersiveMapsConfig.sendStructureWaypoints)
                    continue;

                BlockPos pos = accurate.get(entry.getKey());
                if (pos == null) {
                    double worldX = data.centerX + (decoration.x() / 2.0 * centerOffset);
                    double worldZ = data.centerZ + (decoration.y() / 2.0 * centerOffset);
                    pos = new BlockPos((int) worldX, 100, (int) worldZ);
                }

                if (pos.getY() == 100) {
                    int height = level.getHeight(Heightmap.Types.WORLD_SURFACE, pos.getX(), pos.getZ());
                    if (height > level.getMinBuildHeight()) {
                        pos = new BlockPos(pos.getX(), height + 1, pos.getZ());
                    }
                }

                String name = decoration.name().map(c -> c.getString()).orElse("");
                if (name.isEmpty()) {
                    name = WaypointUtils.getStructureDisplayName(typePath);
                }

                int color = ImmersiveMapsConfig.getStructureColor(typePath);
                if (typePath.contains("banner")) {
                    color = ColorHelper.getHexFromMarkerColor(typePath);
                }

                ResourceLocation texture = WaypointUtils.getIconForType(typePath);

                WayfarerRegistry.WaypointType wpType = ImmersiveMapsConfig.waypointDisplayType == ImmersiveMapsConfig.WaypointDisplayType.FOLD
                        ? WayfarerRegistry.WaypointType.FOLDED
                        : WayfarerRegistry.WaypointType.STANDARD;
                WayfarerRegistry.LocatorType locType = ImmersiveMapsConfig.showLocatorMarkers
                        ? WayfarerRegistry.LocatorType.ICON
                        : WayfarerRegistry.LocatorType.HIDDEN;

                WayfarerRegistry.sendWaypoint(player, name, pos, texture, color, wpType, locType);
            }
        }
    }

    private static boolean isSupported(String type) {
        return type.contains("village") || type.contains("temple") || type.contains("monument")
                || type.contains("mansion") || type.contains("banner") || type.contains("trial_chambers")
                || type.contains("red_x") || type.contains("swamp_hut");
    }
}
