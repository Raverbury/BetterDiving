package meldexun.better_diving.event;

import meldexun.better_diving.BetterDiving;
import meldexun.better_diving.capability.inventory.item.CapabilityItemHandlerItem;
import meldexun.better_diving.capability.inventory.item.CapabilityItemHandlerItemProvider;
import meldexun.better_diving.entity.EntitySeamoth;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemStackHandler;

@Mod.EventBusSubscriber(modid = BetterDiving.MOD_ID)
public class CapabilityEventHandler {

    @SubscribeEvent
    public static void onAttachEntityCapabilitiesEvent(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof EntitySeamoth) {
            event.addCapability(
                    CapabilityItemHandlerItemProvider.REGISTRY_NAME,
                    // new ResourceLocation(BetterDiving.MOD_ID, "test"),
                    new CapabilityItemHandlerItemProvider(
                            () -> new ItemStackHandler(1)));

        }
    }

}
