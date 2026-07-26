package de.ambertation.wunderlib.network;

import net.minecraft.server.level.ServerPlayer;


public abstract class ClientBoundNetworkPayload<T extends ClientBoundNetworkPayload<T>> extends NetworkPayload<T> {
    protected ClientBoundNetworkPayload(PacketHandler<T> packetHandler) {
        super(packetHandler);
    }

    protected abstract void prepareOnServer(ServerPlayer player);

    protected abstract void processOnClient(PacketSender responseSender);

    /**
     * Kept untyped so payload implementations remain loadable on a dedicated
     * server where client Minecraft classes do not exist.
     */
    protected abstract void processOnGameThread(Object client);
}
