package com.wayfarer.immersivemaps.mixin;

import com.wayfarer.immersivemaps.util.IMapItemSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(MapItemSavedData.class)
public abstract class MapItemSavedDataMixin implements IMapItemSavedData {
    @Shadow
    @Final
    public Map<String, MapDecoration> decorations;

    @Unique
    private final Map<String, BlockPos> markers_accuratePos = new HashMap<>();

    @Override
    public Map<String, BlockPos> markers_getAccuratePositions() {
        return markers_accuratePos;
    }

    @Override
    public Map<String, MapDecoration> markers_getDecorations() {
        return decorations;
    }

    @Inject(method = "addDecoration", at = @At("TAIL"))
    private void onAddDecoration(Holder<MapDecorationType> type, LevelAccessor level, String id, double x, double z,
            double rotation, Component name, CallbackInfo ci) {
        markers_accuratePos.put(id, new BlockPos(
                Mth.floor(x),
                100,
                Mth.floor(z)));
    }

    @Inject(method = "removeDecoration", at = @At("TAIL"))
    private void onRemoveDecoration(String id, CallbackInfo ci) {
        markers_accuratePos.remove(id);
    }
}
