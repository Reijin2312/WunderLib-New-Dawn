package de.ambertation.wunderlib.network;

import de.ambertation.wunderlib.utils.EnvHelper;

import net.minecraft.resources.ResourceLocation;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import org.jetbrains.annotations.ApiStatus;

public class ServerBoundPacketHandler<T extends ServerBoundNetworkPayload<T>> extends PacketHandler<T> {
    private static SendToServerAdapter sendToServerAdapter;

    @ApiStatus.Internal
    static void registerAdapter(SendToServerAdapter adapter) {
        ServerBoundPacketHandler.sendToServerAdapter = adapter;
    }

    public ServerBoundPacketHandler(
            ResourceLocation channel,
            NetworkPayload.NetworkPayloadFactory<T> factory
    ) {
        super(channel, factory);
    }

    public static <T extends ServerBoundNetworkPayload<T>> void register(
            ServerBoundPacketHandler<T> packetHandler
    ) {
        PayloadTypeRegistry.playC2S().register(packetHandler.CHANNEL, packetHandler.STREAM_CODEC);

        ServerPlayConnectionEvents.INIT.register((handler, server) -> {
            ServerPlayNetworking.registerReceiver(
                    handler,
                    packetHandler.CHANNEL,
                    packetHandler::receiveOnServer
            );
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            ServerPlayNetworking.unregisterReceiver(handler, packetHandler.CHANNEL.id());
        });
    }

    public static <T extends ServerBoundNetworkPayload<T>> ServerBoundPacketHandler<T> register(
            ResourceLocation channel,
            NetworkPayload.NetworkPayloadFactory<T> factory
    ) {
        ServerBoundPacketHandler<T> packetHandler = new ServerBoundPacketHandler<>(channel, factory);
        register(packetHandler);
        return packetHandler;
    }

    public static <T extends ServerBoundNetworkPayload<T>> void sendToServer(T payload) {
        if (EnvHelper.isClient() && sendToServerAdapter != null) {
            payload.prepareOnClient();
            sendToServerAdapter.sendToServer(payload);
        } else {
            //
        }
    }

    private void receiveOnServer(
            T payload,
            ServerPlayNetworking.Context context
    ) {
        payload.processOnServer(context.player(), context.responseSender());

        final Runnable runner = () -> payload.processOnGameThread(context.player().getServer(), context.player());
        final var server = context
                .player()
                .getServer();
        if (server != null) {
            if (payload.isBlocking()) server.executeBlocking(runner);
            else server.execute(runner);
        }
    }


}
