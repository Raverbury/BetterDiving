package meldexun.better_diving.capability.energy.item;

import meldexun.better_diving.BetterDiving;
import meldexun.better_diving.capability.BasicCapabilityProviderSerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.energy.IEnergyStorage;

public class CapabilityEnergyStorageItemProvider extends BasicCapabilityProviderSerializable<IEnergyStorage> {

	public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(BetterDiving.MOD_ID, "energy_storage");

	public CapabilityEnergyStorageItemProvider(NonNullSupplier<IEnergyStorage> instanceSupplier) {
		super(ForgeCapabilities.ENERGY, instanceSupplier);
	}

	@Override
	public CompoundTag serializeNBT() {
		IEnergyStorage energyCapability =
				this.instance.orElseThrow(NullPointerException::new);
		return ((CapabilityEnergyStorageItem) energyCapability).serializeNBT();
	}

	@Override
	public void deserializeNBT(CompoundTag compoundTag) {
		IEnergyStorage energyCapability =
				this.instance.orElseThrow(NullPointerException::new);
		((CapabilityEnergyStorageItem) energyCapability).deserializeNBT(compoundTag);
	}
}
