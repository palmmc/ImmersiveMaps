package com.wayfarer.immersivemaps;

import com.wayfarer.immersivemaps.config.ImmersiveMapsConfig;

public class CommonClass {
    public static void init() {
        Constants.LOG.info("Initializing ImmersiveMaps...");
        ImmersiveMapsConfig.load();

        Constants.LOG.info("ImmersiveMaps successfully loaded.");
    }
}
