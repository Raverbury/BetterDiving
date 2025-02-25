package meldexun.better_diving.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import meldexun.better_diving.entity.EntitySeamoth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
interface AccessorEntity {
    @Invoker("getVehicle")
    Entity betterdiving$invokeGetVehicle();
}

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity implements AccessorEntity {
    @ModifyReturnValue(
            method = "canBreatheUnderwater",
            at = @At(value = "RETURN")
    )
    public boolean betterdiving$checkBreatheInSeamoth(boolean original) {
        if (original) {
            return true;
        }
        Entity vehicle = this.betterdiving$invokeGetVehicle();
        if (vehicle instanceof EntitySeamoth) {
            return ((EntitySeamoth)vehicle).hasEnergy();
        }
        return false;
    }
}
