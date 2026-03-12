package ch.asarix.wecrazy.client.shaders;

import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderPass;
import net.minecraft.client.Minecraft;

import java.util.List;

public final class SmokeShader extends AnimatedShader {
    public static final SmokeShader INSTANCE = new SmokeShader();


    float fogStrength;
    float fogScale;
    float fogR;
    float fogG;
    float fogB;
    float fogSpeed;
    float seed;
    float detailStrength;
    float heightBias;
    float distanceStart;

    private SmokeShader() {
        super("smoke", "SmokeUniforms");
        fogStrength = 1.0F;
        fogScale = 1.0F;
        fogR = 3.0F;
        fogG = 0.55F;
        fogB = 0.76F;
        fogSpeed = 0.25F;
        seed = 17.0F;
        detailStrength = 0.18F;
        heightBias = 0.35F;
        distanceStart = 0.25F;
    }

    @Override
    protected Std140SizeCalculator extendUboLayout(Std140SizeCalculator calc) {
        return calc
                .putFloat() // FogStrength
                .putFloat() // FogScale
                .putFloat() // FogSpeed
                .putFloat(); // Seed
    }

    @Override
    protected void writeExtraUniforms(Std140Builder builder, Minecraft mc) {
        builder
                .putFloat(fogStrength)
                .putFloat(fogScale)
                .putFloat(fogR)
                .putFloat(fogG);
    }

    @Override
    protected List<String> extraSamplerNames() {
        return List.of("DepthSampler");
    }

    @Override
    protected void bindExtraSamplers(RenderPass pass, RenderTarget mainTarget, RenderTarget inputTarget) {
        pass.bindSampler("DepthSampler", mainTarget.getDepthTextureView());
    }

    @Override
    protected boolean useCopiedInputTarget() {
        return true;
    }
}