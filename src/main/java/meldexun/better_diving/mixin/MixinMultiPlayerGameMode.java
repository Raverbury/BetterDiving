package meldexun.better_diving.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import meldexun.better_diving.block.BlockUnderwaterOre;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MultiPlayerGameMode.class)
public class MixinMultiPlayerGameMode {
    @Shadow
    @Final
    private Minecraft minecraft;

    /**
     * This is to combat the * 0.5 to pitch which makes outcrop hitting sounds
     * sound wrong
     * @param original
     * @param blockPos
     * @return
     */
    @ModifyExpressionValue(
            method = "continueDestroyBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/SoundType;getPitch()F")
    )
    private float betterdiving$playOutcropHitSoundsAtOriginalPitch(float original,
                                                                   @Local(argsOnly = true) BlockPos blockPos) {
        if (this.minecraft.level.getBlockState(blockPos)
                .getBlock() instanceof BlockUnderwaterOre) {
            return original * 2f;
        }
        return original;
    }
}
