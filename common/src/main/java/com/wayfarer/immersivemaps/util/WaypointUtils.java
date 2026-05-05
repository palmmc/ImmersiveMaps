package com.wayfarer.immersivemaps.util;

import net.minecraft.resources.Identifier;

public class WaypointUtils {
    public static Identifier getIconForType(String type) {
        String path = type;
        if (path.startsWith("banner_")) {
            path = path.substring(7) + "_banner";
        }
        return Identifier.fromNamespaceAndPath("minecraft", "textures/map/decorations/" + path + ".png");
    }

    public static String getStructureDisplayName(String type) {
        if (type.equals("red_x"))
            return "Buried Treasure";

        String name = type.replace("_", " ");
        String[] words = name.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (word.length() > 0) {
                sb.append(word.substring(0, 1).toUpperCase());
                sb.append(word.substring(1));
                sb.append(" ");
            }
        }
        return sb.toString().trim();
    }
}
