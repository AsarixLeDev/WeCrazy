package ch.asarix.wecrazy.network;

import ch.asarix.wecrazy.Wecrazy;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record PoopPayload() implements CustomPacketPayload {
    public static final Type<PoopPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Wecrazy.MODID, "poop"));

    public static final StreamCodec<ByteBuf, PoopPayload> STREAM_CODEC =
            StreamCodec.unit(new PoopPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}