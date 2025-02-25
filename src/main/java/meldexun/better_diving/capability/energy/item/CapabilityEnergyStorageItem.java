package meldexun.better_diving.capability.energy.item;

import meldexun.better_diving.api.capability.IEnergyStorageExtended;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import org.joml.Math;

public class CapabilityEnergyStorageItem implements IEnergyStorageExtended,
        INBTSerializable<CompoundTag> {

    private final ItemStack stack;
    protected int capacity;
    protected int maxReceive;
    protected int maxExtract;

    public CapabilityEnergyStorageItem(ItemStack stack, int capacity, int maxReceive, int maxExtract, int energy) {
        this.stack = stack;
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        CompoundTag nbt = this.stack.getTag();
        if (nbt == null || !nbt.contains(
                CapabilityEnergyStorageItemProvider.REGISTRY_NAME.toString(),
                Tag.TAG_INT)) {
            this.setEnergy(energy);
        }
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!this.canReceive() || maxReceive <= 0) {
            return 0;
        }
        int energyReceived = Math.min(maxReceive,
                Math.min(this.getMaxEnergyStored() - this.getEnergyStored(),
                        this.maxReceive));
        if (!simulate) {
            this.setEnergy(this.getEnergyStored() + energyReceived);
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!this.canExtract() || maxExtract <= 0) {
            return 0;
        }
        int energyExtracted = Math.min(maxExtract,
                Math.min(this.getEnergyStored(), this.maxExtract));
        if (!simulate) {
            this.setEnergy(this.getEnergyStored() - energyExtracted);
        }
        return energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        CompoundTag nbt = this.stack.getTag();
        return nbt != null ? nbt.getInt(
                CapabilityEnergyStorageItemProvider.REGISTRY_NAME.toString()) : 0;
    }

    @Override
    public int getMaxEnergyStored() {
        return this.capacity;
    }

    @Override
    public boolean canExtract() {
        return this.maxExtract > 0;
    }

    @Override
    public boolean canReceive() {
        return this.maxReceive > 0;
    }

    @Override
    public void setEnergy(int energy) {
        CompoundTag nbt = this.stack.getOrCreateTag();
        nbt.putInt(CapabilityEnergyStorageItemProvider.REGISTRY_NAME.toString(),
                Math.clamp(energy, 0, this.getMaxEnergyStored()));
    }

    @Override
    public double getEnergyPercent() {
        return (double) this.getEnergyStored() / (double) this.getMaxEnergyStored();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = this.stack.getOrCreateTag();
        if (!nbt.contains(
                CapabilityEnergyStorageItemProvider.REGISTRY_NAME.toString())) {
            nbt.putInt(
                    CapabilityEnergyStorageItemProvider.REGISTRY_NAME.toString(),
                    0);
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag compoundTag) {
        if (compoundTag == null) {
            compoundTag = new CompoundTag();
        }
        int energy = 0;
        if (compoundTag.contains(
                CapabilityEnergyStorageItemProvider.REGISTRY_NAME.toString())) {
            energy =
                    compoundTag.getInt(
                            CapabilityEnergyStorageItemProvider.REGISTRY_NAME.toString());
        }
        setEnergy(energy);
    }
}
