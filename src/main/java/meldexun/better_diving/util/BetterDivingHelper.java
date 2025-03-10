package meldexun.better_diving.util;

import meldexun.better_diving.BetterDiving;
import meldexun.better_diving.api.event.PlayerCanBreathEvent;
import meldexun.better_diving.api.event.PlayerSwimSpeedEvent;
import meldexun.better_diving.config.BetterDivingConfig;
import meldexun.better_diving.init.BetterDivingItems;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;

public class BetterDivingHelper {

    public static double getSwimSpeedRespectEquipment(Player player) {
        double swimSpeed = BetterDivingConfig.SERVER_CONFIG.movement.baseSwimSpeed.get();
        swimSpeed *= player.getAttribute(ForgeMod.SWIM_SPEED.get()).getValue();

        PlayerSwimSpeedEvent event = new PlayerSwimSpeedEvent(player,
                swimSpeed);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getNewSwimSpeed();
    }

    public static boolean canBreath(Player player) {
        boolean canBreath = !player.isEyeInFluid(FluidTags.WATER);
        canBreath |= player.level().getBlockState(player.blockPosition())
                .is(Blocks.BUBBLE_COLUMN);
        canBreath |= player.isCreative();
        canBreath |= player.canBreatheUnderwater();
        canBreath |= MobEffectUtil.hasWaterBreathing(player);

        PlayerCanBreathEvent event = new PlayerCanBreathEvent(player,
                canBreath);
        MinecraftForge.EVENT_BUS.post(event);
        return event.canBreath();
    }

    public static int blocksUnderWater(Entity entity) {
        return blocksUnderWater(entity.level(), new BlockPos(
                (int) entity.getX(),
                (int) (entity.getY() + entity.getEyeHeight()),
                (int) entity.getZ()));
    }

    public static int blocksUnderWater(Level world, BlockPos pos) {
        int i = 0;
        if (world.getFluidState(pos).is(FluidTags.WATER)) {
            while (!world.getBlockState(pos.above(i)).isAir()) {
                i++;
            }
        }
        return i;
    }

    public static Vec3 getMoveVec(double forward, double up, double strafe,
                                  double speed, double yaw, double pitch) {
        double d = forward * forward + up * up + strafe * strafe;
        if (d >= 1.0E-7D) {
            Vec3 vecForward = fromPitchYaw(forward < 0.0D ? -pitch : pitch,
                    forward < 0.0D ? yaw + 180.0D : yaw).scale(
                    Math.abs(forward));
            Vec3 vecUp = fromPitchYaw(
                    (up < 0.0D ? 1.0D : -1.0D) * (90.0D + ((pitch + 90.0D) % 180.0D == 0.0D ? -0.0078125D : 0.0D)),
                    yaw).scale(Math.abs(up));
            Vec3 vecStrafe = fromPitchYaw(0.0D,
                    yaw + (strafe < 0.0D ? 90.0D : -90.0D)).scale(
                    Math.abs(strafe));
            double d1 = d < 1.0D ? Math.sqrt(d) : 1.0D;
            return vecForward.add(vecUp).add(vecStrafe).normalize()
                    .scale(d1 * speed);
        }
        return Vec3.ZERO;
    }

    public static Vec3 getSeamothMoveVec(double forward, double up,
                                         double strafe, double speed, double yaw, double pitch) {
        double d = forward * forward + up * up + strafe * strafe;
        if (d >= 1.0E-7D) {
            Vec3 vecForward = fromPitchYaw(forward < 0.0D ? -pitch : pitch,
                    forward < 0.0D ? yaw + 180.0D : yaw).scale(
                    Math.abs(forward));
            // Vec3 vecUp = fromPitchYaw(pitch + (up < 0.0D ? 90.0D : -90.0D),
            //         yaw).scale(Math.abs(up));
            Vec3 vecUp = new Vec3(0f, up, 0f).scale(Math.abs(up));
            Vec3 vecStrafe = fromPitchYaw(0.0D,
                    yaw + (strafe < 0.0D ? 90.0D : -90.0D)).scale(
                    Math.abs(strafe));
            double d1 = d < 1.0D ? Math.sqrt(d) : 1.0D;
            return vecForward.add(vecUp).add(vecStrafe).normalize()
                    .scale(d1 * speed);
        }
        return Vec3.ZERO;
    }

    private static Vec3 fromPitchYaw(double pitch, double yaw) {
        double d1 = Mth.cos((float) Math.toRadians(-yaw - 180.0D));
        double d2 = Mth.sin((float) Math.toRadians(-yaw - 180.0D));
        double d3 = -Mth.cos((float) Math.toRadians(-pitch));
        double d4 = Mth.sin((float) Math.toRadians(-pitch));
        return new Vec3(d2 * d3, d4, d1 * d3);
    }

    public static boolean canEquipModule(BetterDiving.Vehicle vehicle, ItemStack itemStack) {
        return switch (vehicle) {
            case Seamoth -> itemStack.is(
                    BetterDivingItems.VEHICLE_ENGINE_EFFICIENCY_MODULE.get()) || itemStack.is(
                    BetterDivingItems.VEHICLE_STORAGE_MODULE.get());
            default -> false;
        };
    }

}
