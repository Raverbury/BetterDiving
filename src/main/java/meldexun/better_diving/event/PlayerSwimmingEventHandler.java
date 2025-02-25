package meldexun.better_diving.event;

import meldexun.better_diving.BetterDiving;
import meldexun.better_diving.client.ClientBetterDiving;
import meldexun.better_diving.config.BetterDivingConfig;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BetterDiving.MOD_ID)
public class PlayerSwimmingEventHandler {

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void onInputUpdateEvent(MovementInputUpdateEvent event) {
		Player player = event.getEntity();

		if (player.level().isClientSide()) {
			player.yya = 0.0F;

			if (BetterDivingConfig.SERVER_CONFIG.movementChanges.get()) {
				if (player.isInWater()) {
					Input input = ((LocalPlayer) player).input;
					if (input.jumping) {
						player.yya += 1.0F;
					}
					if (ClientBetterDiving.KEY_BIND_DESCEND.isDown()) {
						player.yya -= 1.0F;
					} else if (input.shiftKeyDown) {
						player.yya -= BetterDivingConfig.SERVER_CONFIG.movement.weakerSneakDescending.get() ? 0.15F : 1.0F;
					}
					player.yya *= 0.98F;
				}
			}
		}
	}

}
