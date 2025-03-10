package meldexun.better_diving.capability.inventory.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

public class CapabilityItemHandlerItem extends CapabilityItemHandlerSeamoth {

	private final ItemStack stack;
	private boolean hasBeenDeserialized = false;

	public CapabilityItemHandlerItem(ItemStack stack) {
		super();
		this.stack = stack;
		CompoundTag nbt = this.stack.getOrCreateTag();
		if (nbt.contains(CapabilityItemHandlerItemProvider.REGISTRY_NAME.toString(), Tag.TAG_INT)) {
			this.deserializeNBT(null);
		} else {
			this.serializeNBT();
		}
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		if (!this.hasBeenDeserialized) {
			this.deserializeNBT(null);
		}
		return super.getStackInSlot(slot);
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag nbt = this.stack.getOrCreateTag();
		nbt.put(CapabilityItemHandlerItemProvider.REGISTRY_NAME.toString(), super.serializeNBT());
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		if (nbt == null) {
			nbt = this.stack.getOrCreateTag();
		}
		// CompoundTag nbt1 = this.stack.getOrCreateTag();
		super.deserializeNBT(nbt.getCompound(CapabilityItemHandlerItemProvider.REGISTRY_NAME.toString()));
		this.hasBeenDeserialized = true;
	}
}
