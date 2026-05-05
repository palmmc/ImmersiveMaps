package com.wayfarer.immersivemaps.platform;

import com.wayfarer.immersivemaps.platform.services.IPlatformHelper;

public class NeoForgePlatformHelper implements IPlatformHelper {
    @Override
    public java.nio.file.Path getConfigFolder() {
        return net.neoforged.fml.loading.FMLPaths.CONFIGDIR.get();
    }
}
