package ch.asarix.wecrazy.network;

import ch.asarix.wecrazy.WeCrazy;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SneakPayload() implements CustomPacketPayload {
    public static final Type<SneakPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(WeCrazy.MODID, "sneak"));

    public static final StreamCodec<ByteBuf, SneakPayload> STREAM_CODEC =
            StreamCodec.unit(new SneakPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}