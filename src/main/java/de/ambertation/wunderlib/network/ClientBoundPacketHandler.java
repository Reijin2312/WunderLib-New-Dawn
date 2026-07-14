package de.ambertation.wunderlib.network;

import de.ambertation.wunderlib.utils.EnvHelper;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.jetbrains.annotations.ApiStatus;

public class ClientBoundPacketHandler<T extends ClientBoundNetworkPayload<T>> extends PacketHandler<T> {
    static List<ClientBoundPacketHandler<?>> packetHandlers = new LinkedList<>();
    private static SendToClientAdapter sendToClientAdapter;

    @ApiStatus.Internal
    static void registerAdapter(SendToClientAdapter adapter) {
        ClientBoundPacketHandler.sendToClientAdapter = adapter;
        for (ClientBoundPacketHandler<?> packetHandler : packetHandlers) {
            adapter.setupConnectionHandler(packetHandler);
        }
        packetHandlers.clear();
    }

    public ClientBoundPacketHandler(
            ResourceLocation channel,
            NetworkPayload.NetworkPayloadFactory<T> factory
    ) {
        super(channel, factory);
    }

    public static <T extends ClientBoundNetworkPayload<T>> void register(
            ClientBoundPacketHandler<T> packetHandler
    ) {
        PayloadTypeRegistry.playS2C().register(packetHandler.CHANNEL, packetHandler.STREAM_CODEC);

        if (sendToClientAdapter != null) {
            sendToClientAdapter.setupConnectionHandler(packetHandler);
        } else {
            packetHandlers.add(packetHandler);
        }
    }

    public static <T extends ClientBoundNetworkPayload<T>> ClientBoundPacketHandler<T> register(
            ResourceLocation channel,
            NetworkPayload.NetworkPayloadFactory<T> factory
    ) {
        ClientBoundPacketHandler<T> packetHandler = new ClientBoundPacketHandler<>(channel, factory);
        register(packetHandler);
        return packetHandler;
    }

    public static <T extends ClientBoundNetworkPayload<T>> void sendToClient(ServerPlayer player, T payload) {
        if (!EnvHelper.isClient()) {
            payload.prepareOnServer(player);
            ServerPlayNetworking.send(player, payload);
        } else {
            //
        }
    }

    public static <T extends ClientBoundNetworkPayload<T>> void sendToClient(
            ServerLevel serverLevel,
            T payload
    ) {
        if (!EnvHelper.isClient()) {
            sendToClient(serverLevel.players(), payload);
        } else {
            //
        }
    }

    public static <T extends ClientBoundNetworkPayload<T>> void sendToClient(
            Collection<ServerPlayer> players,
            T payload
    ) {
        if (!EnvHelper.isClient()) {
            players.forEach(player -> {
                payload.prepareOnServer(player);
                ServerPlayNetworking.send(player, payload);
            });
        } else {
            //
        }
    }
}