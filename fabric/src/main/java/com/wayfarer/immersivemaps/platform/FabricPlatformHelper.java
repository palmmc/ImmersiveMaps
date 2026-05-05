package com.wayfarer.immersivemaps.platform;

import com.wayfarer.immersivemaps.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

public class FabricPlatformHelper implements IPlatformHelper {
    @Override
    public java.nio.file.Path getConfigFolder() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
