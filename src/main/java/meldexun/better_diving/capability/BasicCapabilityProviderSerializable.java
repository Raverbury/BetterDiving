package meldexun.better_diving.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.NonNullSupplier;

public abstract class BasicCapabilityProviderSerializable<C> extends BasicCapabilityProvider<C> implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {

	public BasicCapabilityProviderSerializable(Capability<C> capability, NonNullSupplier<C> instanceSupplier) {
		super(capability, instanceSupplier);
	}
}
