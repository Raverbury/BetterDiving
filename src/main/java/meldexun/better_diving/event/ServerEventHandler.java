package meldexun.better_diving.event;

import meldexun.better_diving.BetterDiving;
import meldexun.better_diving.entity.EntityPowerCellPoweredVehicle;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.living.LivingBreatheEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BetterDiving.MOD_ID)
public class ServerEventHandler {
    @SubscribeEvent
    public static void onServerStartedEvent(ServerStartingEvent event) {
        // DivingGearManager.reloadConfigs();
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onLivingBreatheEvent(LivingBreatheEvent event) {
        Entity vehicle = event.getEntity().getVehicle();
        if (event.getEntity().getVehicle() instanceof EntityPowerCellPoweredVehicle) {
            if (((EntityPowerCellPoweredVehicle) vehicle).hasEnergy()) {
                event.setCanBreathe(true);
                event.setCanRefillAir(true);
            }
        }
    }
}
