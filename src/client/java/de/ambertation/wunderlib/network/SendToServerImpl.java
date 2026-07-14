package de.ambertation.wunderlib.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import org.jetbrains.annotations.ApiStatus;

public class SendToServerImpl implements SendToServerAdapter {
    @Override
    public void sendToServer(ServerBoundNetworkPayload<?> payload) {
        ClientPlayNetworking.send(payload);
    }

    @ApiStatus.Internal
    public static void registerAdapter() {
        ServerBoundPacketHandler.registerAdapter(new SendToServerImpl());
    }
}
