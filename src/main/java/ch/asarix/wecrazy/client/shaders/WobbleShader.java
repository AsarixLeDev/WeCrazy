package ch.asarix.wecrazy.client.shaders;

import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import net.minecraft.client.Minecraft;

public final class WobbleShader extends AnimatedShader {
    public static final WobbleShader INSTANCE = new WobbleShader();

    private float strength = 1.0F;

    private WobbleShader() {
        super("wobble", "WobbleUniforms");
    }

    @Override
    protected Std140SizeCalculator extendUboLayout(Std140SizeCalculator calc) {
        return calc.putFloat(); // Strength
    }

    @Override
    protected void writeExtraUniforms(Std140Builder builder, Minecraft mc) {
        builder.putFloat(strength);
    }

    public void setStrength(float strength) {
        this.strength = strength;
    }
}