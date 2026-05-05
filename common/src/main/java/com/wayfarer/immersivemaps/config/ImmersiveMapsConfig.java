package com.wayfarer.immersivemaps.config;

import com.wayfarer.immersivemaps.platform.Services;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ImmersiveMapsConfig {
    public enum WaypointDisplayType {
        STANDARD, FOLD
    }

    // Client Configuration
    public static WaypointDisplayType waypointDisplayType = WaypointDisplayType.STANDARD;
    public static int compassWaypointColor = 0xFFFFFF;
    public static int recoveryCompassWaypointColor = 0x888888;
    public static boolean showLocatorMarkers = true;

    // Structure Colors
    public static int villageWaypointColor = 0xFFFFFF;
    public static int templeWaypointColor = 0xFFFFFF;
    public static int monumentWaypointColor = 0xFFFFFF;
    public static int mansionWaypointColor = 0xFFFFFF;
    public static int trialChambersWaypointColor = 0xFFFFFF;
    public static int buriedTreasureWaypointColor = 0xFFFFFF;
    public static int swampHutWaypointColor = 0xFFFFFF;

    // Server Configuration
    public static boolean sendStructureWaypoints = true;
    public static boolean sendBannerWaypoints = true;

    private static final String FILE_NAME = "immersivemaps.toml";

    public static void load() {
        Path configPath = Services.PLATFORM.getConfigFolder().resolve(FILE_NAME);
        if (!Files.exists(configPath)) {
            save();
            return;
        }

        try {
            List<String> lines = Files.readAllLines(configPath, StandardCharsets.UTF_8);
            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#"))
                    continue;

                String[] parts = line.split("=", 2);
                if (parts.length < 2)
                    continue;

                String key = parts[0].trim();
                String value = parts[1].trim();

                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }

                try {
                    switch (key) {
                        case "waypointDisplayType" ->
                            waypointDisplayType = WaypointDisplayType.valueOf(value.toUpperCase());
                        case "compassWaypointColor" -> compassWaypointColor = parseColor(value, 0xFFFFFF);
                        case "recoveryCompassWaypointColor" ->
                            recoveryCompassWaypointColor = parseColor(value, 0x888888);
                        case "showLocatorMarkers" -> showLocatorMarkers = Boolean.parseBoolean(value);

                        case "villageWaypointColor" -> villageWaypointColor = parseColor(value, 0xFFFFFF);
                        case "templeWaypointColor" -> templeWaypointColor = parseColor(value, 0xFFFFFF);
                        case "monumentWaypointColor" -> monumentWaypointColor = parseColor(value, 0xFFFFFF);
                        case "mansionWaypointColor" -> mansionWaypointColor = parseColor(value, 0xFFFFFF);
                        case "trialChambersWaypointColor" -> trialChambersWaypointColor = parseColor(value, 0xFFFFFF);
                        case "buriedTreasureWaypointColor" -> buriedTreasureWaypointColor = parseColor(value, 0xFFFFFF);
                        case "swampHutWaypointColor" -> swampHutWaypointColor = parseColor(value, 0xFFFFFF);

                        case "sendStructureWaypoints" -> sendStructureWaypoints = Boolean.parseBoolean(value);
                        case "sendBannerWaypoints" -> sendBannerWaypoints = Boolean.parseBoolean(value);
                    }
                } catch (Exception ex) {
                    System.err.println("Failed to parse immersivemaps config key '" + key + "': " + ex.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load immersivemaps config: " + e.getMessage());
        }
    }

    public static void save() {
        Path configPath = Services.PLATFORM.getConfigFolder().resolve(FILE_NAME);
        List<String> lines = new ArrayList<>();

        lines.add("# ImmersiveMaps Configuration File");
        lines.add("");
        lines.add("# --- Client Config ---");
        lines.add("# STANDARD (default) or FOLD to use folding waypoints for ImmersiveMaps waypoints.");
        lines.add("waypointDisplayType = \"" + waypointDisplayType.name() + "\"");
        lines.add("");
        lines.add("# Whether or not to show marker waypoints on the locator bar.");
        lines.add("showLocatorMarkers = " + showLocatorMarkers);
        lines.add("");
        lines.add("# Hex color for compass waypoints. Defaults to white.");
        lines.add("compassWaypointColor = \"" + formatColor(compassWaypointColor) + "\"");
        lines.add("");
        lines.add("# Hex color for recovery compass waypoints. Defaults to gray.");
        lines.add("recoveryCompassWaypointColor = \"" + formatColor(recoveryCompassWaypointColor) + "\"");
        lines.add("");
        lines.add("# --- Structure Colors ---");
        lines.add("# Hex color for each structure type. Defaults to white.");
        lines.add("villageWaypointColor = \"" + formatColor(villageWaypointColor) + "\"");
        lines.add("templeWaypointColor = \"" + formatColor(templeWaypointColor) + "\"");
        lines.add("monumentWaypointColor = \"" + formatColor(monumentWaypointColor) + "\"");
        lines.add("mansionWaypointColor = \"" + formatColor(mansionWaypointColor) + "\"");
        lines.add("trialChambersWaypointColor = \"" + formatColor(trialChambersWaypointColor) + "\"");
        lines.add("buriedTreasureWaypointColor = \"" + formatColor(buriedTreasureWaypointColor) + "\"");
        lines.add("swampHutWaypointColor = \"" + formatColor(swampHutWaypointColor) + "\"");
        lines.add("");
        lines.add("# --- Server Config ---");
        lines.add("# Whether to send waypoints for structure markers on maps (e.g. Buried Treasure).");
        lines.add("sendStructureWaypoints = " + sendStructureWaypoints);
        lines.add("");
        lines.add("# Whether to send waypoints for banner markers on maps.");
        lines.add("sendBannerWaypoints = " + sendBannerWaypoints);

        try {
            Files.write(configPath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Failed to save ImmersiveMaps config: " + e.getMessage());
        }
    }

    private static int parseColor(String value, int defaultColor) {
        try {
            if (value.startsWith("#")) {
                value = value.substring(1);
            }
            return (int) Long.parseLong(value, 16);
        } catch (Exception e) {
            return defaultColor;
        }
    }

    private static String formatColor(int color) {
        return String.format("#%06X", color & 0xFFFFFF);
    }

    public static int getStructureColor(String typePath) {
        if (typePath.contains("village"))
            return villageWaypointColor;
        if (typePath.contains("temple"))
            return templeWaypointColor;
        if (typePath.contains("monument"))
            return monumentWaypointColor;
        if (typePath.contains("mansion"))
            return mansionWaypointColor;
        if (typePath.contains("trial_chambers"))
            return trialChambersWaypointColor;
        if (typePath.contains("red_x"))
            return buriedTreasureWaypointColor;
        if (typePath.contains("swamp_hut"))
            return swampHutWaypointColor;
        return 0xFFFFFF;
    }
}
