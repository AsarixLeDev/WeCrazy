package ch.asarix.wecrazy.network;

import ch.asarix.wecrazy.ModBlocks;
import ch.asarix.wecrazy.ModItems;
import ch.asarix.wecrazy.ModSounds;
import ch.asarix.wecrazy.WeCrazy;
import ch.asarix.wecrazy.blocks.PoopyCropBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = WeCrazy.MODID)
public final class NetworkHandler {

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(WeCrazy.MODID);

        registrar.playToServer(
                PoopPayload.TYPE,
                PoopPayload.STREAM_CODEC,
                (payload, ctx) -> {
                    ctx.enqueueWork(() -> {
                        ServerPlayer player = (ServerPlayer) ctx.player();
                        if (player == null) return;

                        if (!player.isCrouching()) return;
                        Level level = player.level();

                        level.playSound(
                                null,
                                player.blockPosition(),
                                ModSounds.FART.get(),
                                SoundSource.AMBIENT,
                                1.0f,
                                1.0f
                        );

                        ItemStack stack = new ItemStack(ModItems.POOPY_SEEDS.get());

                        double x = player.getX();
                        double y = player.getBoundingBox().minY + 0.05;
                        double z = player.getZ();

                        ItemEntity item = new ItemEntity(level, x, y, z, stack);
                        item.setDeltaMovement(0, 0, 0);

                        level.addFreshEntity(item);
                    });
                }
        );

        registrar.playToServer(SneakPayload.TYPE, SneakPayload.STREAM_CODEC,
                ((payload, ctx) -> {
                    Player player = ctx.player();
                    Level level;
                    try {
                        level = player.level();
                    } catch (Exception e) {
                        return;
                    }
                    int y = player.blockPosition().getY();
                    BlockPos feetPos = player.blockPosition().atY(y + 1);
                    BlockState blockState = level.getBlockState(feetPos);
                    if (blockState.is(ModBlocks.POOPY_CROP.get())) {
                        PoopyCropBlock crop = (PoopyCropBlock) blockState.getBlock();
                        crop.fart((ServerLevel) level, feetPos);
                        level.playSound(
                                null,
                                feetPos,
                                ModSounds.FART.get(),
                                SoundSource.AMBIENT,
                                1.0f,
                                1.0f
                        );
                    }
                }));
    }
}