package meldexun.better_diving.event;

import meldexun.better_diving.BetterDiving;
import meldexun.better_diving.api.event.PlayerCanBreathEvent;
import meldexun.better_diving.entity.EntitySeamoth;
import meldexun.better_diving.oxygenprovider.DivingGearManager;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BetterDiving.MOD_ID)
public class ServerEventHandler {

	@SuppressWarnings("deprecation")
	@SubscribeEvent(priority = EventPriority.LOW)
    public static void onEntitySizeEvent(EntityEvent.Size event) {
		if (event.getEntity() instanceof Player && event.getEntity().getVehicle() instanceof EntitySeamoth) {
			event.setNewEyeHeight(event.getNewEyeHeight() * 1.164375F / 1.62F);
		}
	}

	@SubscribeEvent
	public static void onServerStartedEvent(ServerStartingEvent event) {
		// DivingGearManager.reloadConfigs();
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onPlayerWaterBreathingEvent(PlayerCanBreathEvent event) {
		if (event.getPlayer().getVehicle() instanceof EntitySeamoth && ((EntitySeamoth) event.getPlayer().getVehicle()).hasEnergy()) {
			event.setCanBreath(true);
		}
	}

}
