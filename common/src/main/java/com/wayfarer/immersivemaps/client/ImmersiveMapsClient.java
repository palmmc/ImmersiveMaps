package com.wayfarer.immersivemaps.client;

import com.wayfarer.api.WayfarerRegistry;
import com.wayfarer.api.WayfarerRegistry.LocatorType;
import com.wayfarer.api.WayfarerRegistry.WaypointType;
import com.wayfarer.immersivemaps.config.ImmersiveMapsConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.LodestoneTracker;
import net.minecraft.world.level.Level;

public class ImmersiveMapsClient {

    private static final ResourceLocation COMPASS_ICON = ResourceLocation.fromNamespaceAndPath("immersivemaps",
            "textures/map/decorations/compass.png");
    private static final ResourceLocation RECOVERY_COMPASS_ICON = ResourceLocation.fromNamespaceAndPath("immersivemaps",
            "textures/map/decorations/recovery_compass.png");

    public static void init() {
        WayfarerRegistry.registerTransformer(new ImmersiveMapsTransformer());
    }

    public static void onClientTick() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null)
            return;

        WayfarerRegistry.AUTOMATED_PROVIDER.clear();
        checkCompass(mc.player.getMainHandItem());
        checkCompass(mc.player.getOffhandItem());
    }

    private static void checkCompass(ItemStack stack) {
        if (stack.isEmpty())
            return;

        if (stack.is(Items.COMPASS)) {
            LodestoneTracker tracker = stack.get(DataComponents.LODESTONE_TRACKER);
            if (tracker != null) {
                tracker.target().ifPresent(globalPos -> {
                    registerMarker(globalPos, getWaypointName(stack, "Lodestone"), COMPASS_ICON,
                            ImmersiveMapsConfig.compassWaypointColor);
                });
            } else {
                Minecraft mc = Minecraft.getInstance();
                if (mc.level != null && mc.level.dimension() == Level.OVERWORLD) {
                    BlockPos spawn = mc.level.getSharedSpawnPos();
                    WaypointType wpType = ImmersiveMapsConfig.waypointDisplayType == ImmersiveMapsConfig.WaypointDisplayType.FOLD
                            ? WaypointType.FOLDED
                            : WaypointType.STANDARD;
                    LocatorType locType = ImmersiveMapsConfig.showLocatorMarkers
                            ? LocatorType.ICON
                            : LocatorType.HIDDEN;
                    WayfarerRegistry.AUTOMATED_PROVIDER
                            .add(new WayfarerRegistry.Waypoint(getWaypointName(stack, "Spawn"), spawn, COMPASS_ICON,
                                    ImmersiveMapsConfig.compassWaypointColor, wpType, locType));
                }
            }
        } else if (stack.is(Items.RECOVERY_COMPASS)) {
            Minecraft.getInstance().player.getLastDeathLocation().ifPresent(deathPos -> {
                registerMarker(deathPos, getWaypointName(stack, "Last Death"), RECOVERY_COMPASS_ICON,
                        ImmersiveMapsConfig.recoveryCompassWaypointColor);
            });
        }
    }

    private static String getWaypointName(ItemStack stack, String defaultName) {
        if (stack.has(DataComponents.CUSTOM_NAME)) {
            return stack.getHoverName().getString();
        }
        return defaultName;
    }

    private static void registerMarker(GlobalPos globalPos, String name, ResourceLocation icon, int color) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null && mc.level.dimension().equals(globalPos.dimension())) {
            WaypointType wpType = ImmersiveMapsConfig.waypointDisplayType == ImmersiveMapsConfig.WaypointDisplayType.FOLD
                    ? WaypointType.FOLDED
                    : WaypointType.STANDARD;
            LocatorType locType = ImmersiveMapsConfig.showLocatorMarkers
                    ? LocatorType.ICON
                    : LocatorType.HIDDEN;
            WayfarerRegistry.AUTOMATED_PROVIDER
                    .add(new WayfarerRegistry.Waypoint(name, globalPos.pos(), icon, color, wpType, locType));
        }
    }
}
