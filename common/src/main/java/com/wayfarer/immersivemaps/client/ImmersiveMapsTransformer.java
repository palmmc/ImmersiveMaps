package com.wayfarer.immersivemaps.client;

import com.wayfarer.api.WayfarerRegistry;
import com.wayfarer.api.WayfarerRegistry.LocatorType;
import com.wayfarer.api.WayfarerRegistry.WaypointType;
import com.wayfarer.immersivemaps.config.ImmersiveMapsConfig;

public class ImmersiveMapsTransformer implements WayfarerRegistry.WaypointTransformer {
    @Override
    public WayfarerRegistry.Waypoint transform(WayfarerRegistry.Waypoint wp) {
        if (wp.icon == null)
            return wp;

        String namespace = wp.icon.getNamespace();
        String path = wp.icon.getPath();

        boolean isMarker = namespace.equals("immersivemaps");
        boolean isStructure = namespace.equals("minecraft") && path.contains("textures/map/decorations/");
        boolean isPlayer = namespace.equals("wayfarer") && path.equals("player");

        if (isPlayer) {
            net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
            if (mc.player != null && ImmersiveMapsConfig.showPlayersInWorldWithCompass) {
                boolean holdingCompass = mc.player.getMainHandItem().is(net.minecraft.world.item.Items.COMPASS)
                        || mc.player.getMainHandItem().is(net.minecraft.world.item.Items.RECOVERY_COMPASS)
                        || mc.player.getOffhandItem().is(net.minecraft.world.item.Items.COMPASS)
                        || mc.player.getOffhandItem().is(net.minecraft.world.item.Items.RECOVERY_COMPASS);
                if (holdingCompass) {
                    WaypointType type = ImmersiveMapsConfig.waypointDisplayType == ImmersiveMapsConfig.WaypointDisplayType.FOLD
                            ? WaypointType.FOLDED
                            : WaypointType.STANDARD;
                    return new WayfarerRegistry.Waypoint(wp.name, wp.pos, wp.icon, wp.color, type, wp.locatorType);
                }
            }
        }

        if (isMarker || isStructure) {
            WaypointType type = wp.type;
            if (ImmersiveMapsConfig.waypointDisplayType == ImmersiveMapsConfig.WaypointDisplayType.FOLD) {
                type = WaypointType.FOLDED;
            } else {
                type = WaypointType.STANDARD;
            }

            LocatorType locatorType = wp.locatorType;
            if (!ImmersiveMapsConfig.showLocatorMarkers) {
                locatorType = LocatorType.HIDDEN;
            } else if (locatorType == LocatorType.HIDDEN) {
                locatorType = LocatorType.ICON;
            }

            int color = wp.color;
            if (isMarker) {
                if (path.contains("recovery")) {
                    color = ImmersiveMapsConfig.recoveryCompassWaypointColor;
                } else {
                    color = ImmersiveMapsConfig.compassWaypointColor;
                }
            } else if (isStructure) {
                try {
                    String fileName = path.substring(path.lastIndexOf('/') + 1, path.lastIndexOf('.'));
                    color = ImmersiveMapsConfig.getStructureColor(fileName);
                } catch (Exception ignored) {
                }
            }

            return new WayfarerRegistry.Waypoint(wp.name, wp.pos, wp.icon, color, type, locatorType);
        }

        return wp;
    }
}
