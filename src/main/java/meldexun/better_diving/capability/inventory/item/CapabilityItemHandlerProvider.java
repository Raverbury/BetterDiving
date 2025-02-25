package meldexun.better_diving.capability.inventory.item;

import meldexun.better_diving.BetterDiving;
import meldexun.better_diving.capability.BasicCapabilityProviderSerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.items.IItemHandler;

public class CapabilityItemHandlerProvider extends BasicCapabilityProviderSerializable<IItemHandler> {

    public static final ResourceLocation REGISTRY_NAME =
            new ResourceLocation(BetterDiving.MOD_ID, "item_stack_handler");

    public CapabilityItemHandlerProvider(NonNullSupplier<IItemHandler> instanceSupplier) {
        super(ForgeCapabilities.ITEM_HANDLER, instanceSupplier);
    }

    @Override
    public CompoundTag serializeNBT() {
        return null;
    }

    @Override
    public void deserializeNBT(CompoundTag compoundTag) {

    }
}