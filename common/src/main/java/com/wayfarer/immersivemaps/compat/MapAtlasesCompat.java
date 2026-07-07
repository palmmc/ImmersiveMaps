package com.wayfarer.immersivemaps.compat;

import com.wayfarer.immersivemaps.logic.MapMarkerLogic;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import pepjebs.mapatlases.item.MapAtlasItem;
import pepjebs.mapatlases.map_collection.MapCollection;
import pepjebs.mapatlases.utils.MapDataHolder;

public class MapAtlasesCompat {

    public static boolean isAtlas(ItemStack stack) {
        ResourceLocation key = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return key.getNamespace().equals("map_atlases") && key.getPath().equals("atlas");
    }

    public static void collectAtlasMarkers(ServerPlayer player, ItemStack stack, java.util.List<com.wayfarer.api.WayfarerRegistry.Waypoint> waypoints) {
        MapCollection maps = MapAtlasItem.getMaps(stack, player.level());
        if (maps != null) {
            for (MapDataHolder holder : maps.getAllFound()) {
                if (holder.data != null) {
                    MapMarkerLogic.collectMapMarkers(player, holder.data, waypoints);
                }
            }
        }
    }
}
