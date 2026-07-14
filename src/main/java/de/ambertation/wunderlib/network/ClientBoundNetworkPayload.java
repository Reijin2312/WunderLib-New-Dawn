package de.ambertation.wunderlib.network;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;

public abstract class ClientBoundNetworkPayload<T extends ClientBoundNetworkPayload<T>> extends NetworkPayload<T> {
    protected ClientBoundNetworkPayload(PacketHandler<T> packetHandler) {
        super(packetHandler);
    }

    protected abstract void prepareOnServer(ServerPlayer player);

    @Environment(EnvType.CLIENT)
    protected abstract void processOnClient(PacketSender responseSender);

    @Environment(EnvType.CLIENT)
    protected abstract void processOnGameThread(Minecraft client);
}
