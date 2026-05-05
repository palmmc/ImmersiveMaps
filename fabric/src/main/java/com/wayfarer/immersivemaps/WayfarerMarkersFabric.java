package com.wayfarer.immersivemaps;

import com.wayfarer.immersivemaps.client.ImmersiveMapsClient;
import com.wayfarer.immersivemaps.logic.MapMarkerLogic;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class WayfarerMarkersFabric implements ModInitializer, ClientModInitializer {

    @Override
    public void onInitialize() {
        CommonClass.init();

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            server.getPlayerList().getPlayers().forEach(MapMarkerLogic::onServerTick);
        });
    }

    @Override
    public void onInitializeClient() {
        ImmersiveMapsClient.init();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ImmersiveMapsClient.onClientTick();
        });
    }
}
