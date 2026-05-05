package com.wayfarer.immersivemaps.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import java.util.Map;

public interface IMapItemSavedData {
    Map<String, BlockPos> markers_getAccuratePositions();

    Map<String, MapDecoration> markers_getDecorations();
}
