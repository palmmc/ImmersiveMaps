package com.wayfarer.immersivemaps;

import com.wayfarer.immersivemaps.client.ImmersiveMapsClient;
import com.wayfarer.immersivemaps.client.ImmersiveMapsConfigScreen;
import com.wayfarer.immersivemaps.logic.MapMarkerLogic;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@Mod("immersivemaps")
public class WayfarerMarkersNeoForge {

    public WayfarerMarkersNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        CommonClass.init();

        if (FMLEnvironment.dist.isClient()) {
            modContainer.registerExtensionPoint(IConfigScreenFactory.class,
                    (container, screen) -> ImmersiveMapsConfigScreen.create(screen));
        }

        NeoForge.EVENT_BUS.addListener(this::onPlayerTick);

        if (FMLEnvironment.dist.isClient()) {
            ImmersiveMapsClient.init();
        }
    }

    private void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            MapMarkerLogic.onServerTick(serverPlayer);
        } else if (event.getEntity().level().isClientSide()) {
            ImmersiveMapsClient.onClientTick();
        }
    }
}
