package meldexun.better_diving.event;

import meldexun.better_diving.BetterDiving;
import meldexun.better_diving.blockentity.BlockEntityVehicleDock;
import meldexun.better_diving.capability.energy.blockentity.CapabilityEnergyStorageBlockEntityProvider;
import meldexun.better_diving.capability.inventory.item.CapabilityItemHandlerItemProvider;
import meldexun.better_diving.capability.inventory.item.CapabilityItemHandlerSeamoth;
import meldexun.better_diving.config.BetterDivingConfig;
import meldexun.better_diving.entity.EntityPowerCellPoweredVehicle;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemStackHandler;

@Mod.EventBusSubscriber(modid = BetterDiving.MOD_ID)
public class CapabilityEventHandler {

    @SubscribeEvent
    public static void onAttachEntityCapabilitiesEvent(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof EntityPowerCellPoweredVehicle) {
            event.addCapability(
                    CapabilityItemHandlerItemProvider.REGISTRY_NAME,
                    new CapabilityItemHandlerItemProvider(
                            () -> new CapabilityItemHandlerSeamoth()));

        }
    }

    @SubscribeEvent
    public static void onAttachBlockEntityCapabilitiesEvent(AttachCapabilitiesEvent<BlockEntity> event) {
        BlockEntity blockEntity = event.getObject();
        if (blockEntity instanceof BlockEntityVehicleDock) {
            event.addCapability(
                    CapabilityEnergyStorageBlockEntityProvider.REGISTRY_NAME,
                    new CapabilityEnergyStorageBlockEntityProvider(
                            new EnergyStorage(BetterDivingConfig.SERVER_CONFIG.dockingBlock.dockBlockEnergyCapacity.get(), 2000,
                                    1000)));
        }
    }

}
