package meldexun.better_diving.event;

import meldexun.better_diving.BetterDiving;
import meldexun.better_diving.config.BetterDivingConfig;
import meldexun.better_diving.util.DivingGearHelper;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BetterDiving.MOD_ID)
public class PlayerMiningEventHandler {

	@SubscribeEvent
	public static void onPlayerBreakSpeedEvent(PlayerEvent.BreakSpeed event) {
		if (!BetterDivingConfig.SERVER_CONFIG.breakSpeedChanges.get()) {
			return;
		}

		Player player = event.getEntity();

		if (player.isInWater()) {
			float multiplier = 1.0F + (float) DivingGearHelper.getMiningspeedBonus(player);

			if (player.isEyeInFluid(FluidTags.WATER)) {
				ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);
				int aquaAffinityLevel =
						head.getEnchantmentLevel(Enchantments.AQUA_AFFINITY);
				multiplier *= BetterDivingConfig.SERVER_CONFIG.mining.breakSpeedBase.get();
				if (aquaAffinityLevel > 0) {
					multiplier *= (float) (BetterDivingConfig.SERVER_CONFIG.mining.breakSpeedAquaAffinity.get() * aquaAffinityLevel);
				} else {
					multiplier *= 5.0F;
				}
			}

			if (!player.onGround()) {
				multiplier *= 5.0F;
			}

			event.setNewSpeed(event.getNewSpeed() * multiplier);
		}
	}

}
