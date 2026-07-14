package de.ambertation.wunderlib;

import de.ambertation.wunderlib.network.SendToClientImpl;
import de.ambertation.wunderlib.network.SendToServerImpl;

import net.fabricmc.api.ClientModInitializer;

public class WunderLibClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SendToServerImpl.registerAdapter();
        SendToClientImpl.registerAdapter();
    }
}
