package de.ambertation.wunderlib.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.NotNull;

public abstract class PacketHandler<T extends NetworkPayload<T>> {
    @NotNull
    public final CustomPacketPayload.Type<T> CHANNEL;

    @NotNull
    public final StreamCodec<FriendlyByteBuf, T> STREAM_CODEC;

    protected PacketHandler(
            @NotNull ResourceLocation channel,
            @NotNull NetworkPayload.NetworkPayloadFactory<T> factory
    ) {
        this.CHANNEL = CustomPacketPayload.createType(channel.getNamespace() + "_" + channel.getPath());

        this.STREAM_CODEC = CustomPacketPayload.codec(
                NetworkPayload::write,
                factory::create
        );
    }
}
