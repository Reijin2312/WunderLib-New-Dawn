package de.ambertation.wunderlib.network;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;

public abstract class ServerBoundNetworkPayload<T extends ServerBoundNetworkPayload<T>> extends NetworkPayload<T> {
    protected ServerBoundNetworkPayload(PacketHandler<T> packetHandler) {
        super(packetHandler);
    }

    @Environment(EnvType.CLIENT)
    protected abstract void prepareOnClient();

    protected abstract void processOnServer(ServerPlayer player, PacketSender responseSender);

    protected abstract void processOnGameThread(MinecraftServer server, ServerPlayer player);
}
