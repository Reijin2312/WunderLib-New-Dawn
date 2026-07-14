package de.ambertation.wunderlib.network;

import de.ambertation.wunderlib.utils.EnvHelper;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public class SendToClientImpl implements SendToClientAdapter {
    @ApiStatus.Internal
    public static void registerAdapter() {
        ClientBoundPacketHandler.registerAdapter(new SendToClientImpl());
    }

    @Override
    public <T extends ClientBoundNetworkPayload<T>> void setupConnectionHandler(ClientBoundPacketHandler<T> packetHandler) {
        ClientPlayConnectionEvents.INIT.register((handler, server) -> {
            ClientPlayNetworking.registerReceiver(
                    packetHandler.CHANNEL,
                    this::receiveOnClient
            );
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            ClientPlayNetworking.unregisterReceiver(packetHandler.CHANNEL.id());
        });
    }

    <T extends ClientBoundNetworkPayload<T>> void receiveOnClient(
            @NotNull T payload,
            ClientPlayNetworking.Context context
    ) {
        if (!EnvHelper.isClient()) return;

        payload.processOnClient(context.responseSender());
        final var client = context.client();
        
        if (client != null) {
            final Runnable runner = () -> payload.processOnGameThread(client);

            if (payload.isBlocking()) client.executeBlocking(runner);
            else client.execute(runner);
        }
    }
}
