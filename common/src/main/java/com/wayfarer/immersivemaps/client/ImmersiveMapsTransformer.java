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
