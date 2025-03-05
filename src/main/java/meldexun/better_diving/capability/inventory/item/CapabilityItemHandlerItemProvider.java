package meldexun.better_diving.capability.inventory.item;

import meldexun.better_diving.BetterDiving;
import meldexun.better_diving.capability.BasicCapabilityProviderSerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class CapabilityItemHandlerItemProvider extends BasicCapabilityProviderSerializable<IItemHandler> {

    public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(
            BetterDiving.MOD_ID, "item_stack_handler");

    public CapabilityItemHandlerItemProvider(NonNullSupplier<IItemHandler> instanceSupplier) {
        super(ForgeCapabilities.ITEM_HANDLER, instanceSupplier);
    }

    @Override
    public CompoundTag serializeNBT() {
        IItemHandler itemHandler =
                this.instance.orElseThrow(NullPointerException::new);
        return ((ItemStackHandler) itemHandler).serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag compoundTag) {
        IItemHandler itemHandler =
                this.instance.orElseThrow(NullPointerException::new);
        ((ItemStackHandler) itemHandler).deserializeNBT(compoundTag);
    }
}
