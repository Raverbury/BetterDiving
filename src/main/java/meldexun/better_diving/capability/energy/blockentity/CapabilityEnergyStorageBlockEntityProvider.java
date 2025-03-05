package meldexun.better_diving.capability.energy.blockentity;

import meldexun.better_diving.BetterDiving;
import net.minecraft.core.Direction;
import net.minecraft.nbt.IntTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import org.jetbrains.annotations.NotNull;

public class CapabilityEnergyStorageBlockEntityProvider implements ICapabilitySerializable<IntTag> {

	public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(BetterDiving.MOD_ID, "energy_storage");

	private EnergyStorage energyStorage;
	private LazyOptional<EnergyStorage> optionalEnergyStorage;

	public CapabilityEnergyStorageBlockEntityProvider(EnergyStorage energyStorage) {
		super();
		this.energyStorage = energyStorage;
		this.optionalEnergyStorage = LazyOptional.of(() -> energyStorage);
	}

	@Override
	public IntTag serializeNBT() {
		return (IntTag) energyStorage.serializeNBT();
	}

	@Override
	public void deserializeNBT(IntTag tag) {
		energyStorage.deserializeNBT(tag);
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction direction) {
		if (cap == ForgeCapabilities.ENERGY) {
			return optionalEnergyStorage.cast();
		}
		return LazyOptional.empty();
	}
}
