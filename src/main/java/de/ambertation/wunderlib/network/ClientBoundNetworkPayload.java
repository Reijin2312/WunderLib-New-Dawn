package de.ambertation.wunderlib.network;

import net.minecraft.server.level.ServerPlayer;


public abstract class ClientBoundNetworkPayload<T extends ClientBoundNetworkPayload<T>> extends NetworkPayload<T> {
    protected ClientBoundNetworkPayload(PacketHandler<T> packetHandler) {
        super(packetHandler);
    }

    protected abstract void prepareOnServer(ServerPlayer player);

    protected abstract void processOnClient(PacketSender responseSender);

    /**
     * Receives the client instance as an untyped value so common packet
     * payloads can be loaded by a dedicated server without resolving the
     * client-only Minecraft class.
     */
    protected abstract void processOnGameThread(Object client);
}
