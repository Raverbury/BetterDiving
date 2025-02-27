package meldexun.better_diving.mixin;

import meldexun.better_diving.init.BetterDivingSounds;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.AdvancementToast;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AdvancementToast.class)
public class MixinAdvancementToast {

    @Shadow
    @Final
    private Advancement advancement;

    // simpler solution but this forces advancement to be challenge type
    // @ModifyExpressionValue(
    //         method = "render",
    //         at = @At(
    //                 value = "FIELD",
    //                 target = "Lnet/minecraft/sounds/SoundEvents;UI_TOAST_CHALLENGE_COMPLETE:Lnet/minecraft/sounds/SoundEvent;")
    // )
    // private SoundEvent v(SoundEvent original) {
    //     if (this.advancement.getId().toString().equals("better_diving:main" +
    //             "/seamoth")) {
    //         return BetterDivingSounds.ADVANCEMENT_SEAMOTH.get();
    //     }
    //     return original;
    // }

    /**
     * Kinda brittle solution ngl
     * @param p_281813_
     * @param toastComponent
     * @param p_282604_
     * @param cir
     */
    @Inject(
            method = "render",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/components/toasts" +
                            "/AdvancementToast;playedSound:Z",
            opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER)
    )
    private void betterdiving$playCustomSeamothAdvancementSound(GuiGraphics p_281813_, ToastComponent toastComponent,
                   long p_282604_,
                   CallbackInfoReturnable<Toast.Visibility> cir) {
        if (this.advancement.getId().toString().equals("better_diving:main" +
                "/seamoth")) {
            toastComponent.getMinecraft().getSoundManager().play(
                    SimpleSoundInstance.forUI(
                            BetterDivingSounds.ADVANCEMENT_SEAMOTH.get(), 1f,
                            1f));
        }
    }
}
