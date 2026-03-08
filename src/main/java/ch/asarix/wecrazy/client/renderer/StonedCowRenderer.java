package ch.asarix.wecrazy.client.renderer;

import ch.asarix.wecrazy.WeCrazy;
import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.CowRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.CowVariants;

public class StonedCowRenderer extends CowRenderer {
    private static final ResourceLocation TEMPERATE =
            ResourceLocation.fromNamespaceAndPath(WeCrazy.MODID, "textures/entity/cow/stoned_temperate_cow.png");
    private static final ResourceLocation WARM =
            ResourceLocation.fromNamespaceAndPath(WeCrazy.MODID, "textures/entity/cow/stoned_warm_cow.png");
    private static final ResourceLocation COLD =
            ResourceLocation.fromNamespaceAndPath(WeCrazy.MODID, "textures/entity/cow/stoned_cold_cow.png");

    public StonedCowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public CowRenderState createRenderState() {
        return new StonedCowRenderState();
    }

    @Override
    public void extractRenderState(Cow cow, CowRenderState baseState, float partialTick) {
        super.extractRenderState(cow, baseState, partialTick);

        StonedCowRenderState state = (StonedCowRenderState) baseState;

        if (cow.getVariant().is(CowVariants.WARM)) {
            state.variantId = 1;
        } else if (cow.getVariant().is(CowVariants.COLD)) {
            state.variantId = 2;
        } else {
            state.variantId = 0;
        }
    }

    @Override
    public ResourceLocation getTextureLocation(CowRenderState baseState) {
        StonedCowRenderState state = (StonedCowRenderState) baseState;

        return switch (state.variantId) {
            case 1 -> WARM;
            case 2 -> COLD;
            default -> TEMPERATE;
        };
    }
}