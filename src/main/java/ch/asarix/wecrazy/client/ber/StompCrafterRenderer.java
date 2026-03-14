package ch.asarix.wecrazy.client.ber;

import ch.asarix.wecrazy.blocks.entity.StompCrafterBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;

public class StompCrafterRenderer implements BlockEntityRenderer<StompCrafterBlockEntity, StompCrafterRenderState> {
    private final ItemModelResolver itemResolver;
    private final ItemStackRenderState itemRenderState = new ItemStackRenderState();

    public StompCrafterRenderer(BlockEntityRendererProvider.Context context) {
        this.itemResolver = Minecraft.getInstance().getItemModelResolver();
    }

    @Override
    public StompCrafterRenderState createRenderState() {
        return new StompCrafterRenderState();
    }

    @Override
    public void extractRenderState(StompCrafterBlockEntity blockEntity,
                                   StompCrafterRenderState renderState,
                                   float partialTick,
                                   Vec3 cameraPos,
                                   @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPos, crumblingOverlay);

        renderState.displayStack = blockEntity.getDisplayStack().copy();
        renderState.progress = blockEntity.getProgress();

        if (blockEntity.getLevel() != null) {
            renderState.lightCoords = LevelRenderer.getLightColor(blockEntity.getLevel(), blockEntity.getBlockPos().above());
        } else {
            renderState.lightCoords = LightTexture.FULL_BRIGHT;
        }
    }

    @Override
    public void submit(StompCrafterRenderState renderState,
                       PoseStack poseStack,
                       SubmitNodeCollector collector,
                       CameraRenderState cameraState) {
        if (renderState.displayStack.isEmpty()) {
            return;
        }

        this.itemResolver.updateForTopItem(
                this.itemRenderState,
                renderState.displayStack,
                ItemDisplayContext.FIXED,
                null,
                null,
                0
        );

        poseStack.pushPose();
        poseStack.translate(0.5D, 0.35D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(45.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.scale(0.45F, 0.45F, 0.45F);

        this.itemRenderState.submit(
                poseStack,
                collector,
                renderState.lightCoords,
                OverlayTexture.NO_OVERLAY,
                0
        );

        poseStack.popPose();
    }
}