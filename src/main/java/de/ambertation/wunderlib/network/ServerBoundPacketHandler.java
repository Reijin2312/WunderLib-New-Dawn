package de.ambertation.wunderlib.network;

import de.ambertation.wunderlib.utils.EnvHelper;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;

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
            Identifier channel,
            NetworkPayload.NetworkPayloadFactory<T> factory
    ) {
        super(channel, factory);
    }

    public static <T extends ServerBoundNetworkPayload<T>> void register(
            ServerBoundPacketHandler<T> packetHandler
    ) {
        PayloadTypeRegistry.serverboundPlay().register(packetHandler.CHANNEL, packetHandler.STREAM_CODEC);

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
            Identifier channel,
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
        PacketSender responseSender = context.responseSender()::sendPacket;
        payload.processOnServer(context.player(), responseSender);

        final var server = ((ServerLevel) context.player().level()).getServer();
        final Runnable runner = () -> payload.processOnGameThread(server, context.player());
        if (server != null) {
            if (payload.isBlocking()) server.executeBlocking(runner);
            else server.execute(runner);
        }
    }


}
