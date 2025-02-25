package meldexun.better_diving.item;

import java.util.List;

import meldexun.better_diving.api.capability.IEnergyStorageExtended;
import meldexun.better_diving.capability.energy.item.CapabilityEnergyStorageItem;
import meldexun.better_diving.capability.energy.item.CapabilityEnergyStorageItemProvider;
import meldexun.better_diving.config.BetterDivingConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

public class ItemEnergyStorage extends Item {

	protected final BetterDivingConfig.ServerConfig.EnergyStorageItem config;

	public ItemEnergyStorage(BetterDivingConfig.ServerConfig.EnergyStorageItem config) {
		super(new Item.Properties().stacksTo(1));
		this.config = config;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
		return new CapabilityEnergyStorageItemProvider(() -> new CapabilityEnergyStorageItem(stack, this.getCapacity(), this.getMaxReceive(), this.getMaxExtract(), this.getEnergy()));
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		LazyOptional<IEnergyStorage> optionalOxygenCap =
				stack.getCapability(ForgeCapabilities.ENERGY);
		if (!optionalOxygenCap.isPresent()) {
			return 0;
		}
		IEnergyStorage energyCap = optionalOxygenCap.orElseThrow(NullPointerException::new);
		return Math.round((float) energyCap.getEnergyStored() * 13 / (float) energyCap.getMaxEnergyStored());
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		return true;
	}

	@Override
	public void appendHoverText(ItemStack stack, Level worldIn,
								List<Component> tooltip, TooltipFlag flagIn) {
		int energy = Mth.ceil(getEnergyPercent(stack) * 100.0D);
		if (flagIn.isAdvanced()) {
			tooltip.add(Component.literal(ChatFormatting.GRAY + String.format(
					"Energy " +
					"%d%% (%d/%d)", energy, getEnergy(stack), getEnergyCapacity(stack))));
		} else {
			tooltip.add(Component.literal(ChatFormatting.GRAY + String.format(
					"Energy %d%%", energy)));
		}
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
	}

	public static boolean hasEnergy(ItemStack stack) {
		return ItemEnergyStorage.getEnergy(stack) > 0;
	}

	public static int getEnergy(ItemStack stack) {
		if (stack.getItem() instanceof ItemEnergyStorage) {
			LazyOptional<IEnergyStorage> optionalOxygenCap =
					stack.getCapability(ForgeCapabilities.ENERGY);
			if (!optionalOxygenCap.isPresent()) {
				return 0;
			}
			IEnergyStorage energyCap = optionalOxygenCap.orElseThrow(NullPointerException::new);
			return energyCap.getEnergyStored();
		}
		return 0;
	}

	public static boolean setEnergy(ItemStack stack, int energy) {
		if (stack.getItem() instanceof ItemEnergyStorage) {
			LazyOptional<IEnergyStorage> optionalOxygenCap =
					stack.getCapability(ForgeCapabilities.ENERGY);
			if (!optionalOxygenCap.isPresent()) {
				return false;
			}
			IEnergyStorage energyCap = optionalOxygenCap.orElseThrow(NullPointerException::new);
			((IEnergyStorageExtended) energyCap).setEnergy(energy);
			return true;
		}
		return false;
	}

	public static double getEnergyPercent(ItemStack stack) {
		if (stack.getItem() instanceof ItemEnergyStorage) {
			LazyOptional<IEnergyStorage> optionalOxygenCap =
					stack.getCapability(ForgeCapabilities.ENERGY);
			if (!optionalOxygenCap.isPresent()) {
				return 0.0D;
			}
			IEnergyStorage energyCap = optionalOxygenCap.orElseThrow(NullPointerException::new);
			return ((IEnergyStorageExtended) energyCap).getEnergyPercent();
		}
		return 0.0D;
	}

	public static int getEnergyCapacity(ItemStack stack) {
		if (stack.getItem() instanceof ItemEnergyStorage) {
			LazyOptional<IEnergyStorage> optionalOxygenCap =
					stack.getCapability(ForgeCapabilities.ENERGY);
			if (!optionalOxygenCap.isPresent()) {
				return 0;
			}
			IEnergyStorage energyCap = optionalOxygenCap.orElseThrow(NullPointerException::new);
			return energyCap.getMaxEnergyStored();
		}
		return 0;
	}

	public static int receiveEnergy(ItemStack stack, int amount) {
		if (stack.getItem() instanceof ItemEnergyStorage && amount > 0) {
			LazyOptional<IEnergyStorage> optionalOxygenCap =
					stack.getCapability(ForgeCapabilities.ENERGY);
			if (!optionalOxygenCap.isPresent()) {
				return 0;
			}
			IEnergyStorage energyCap = optionalOxygenCap.orElseThrow(NullPointerException::new);
			return energyCap.receiveEnergy(amount, false);
		}
		return 0;
	}

	public static int extractEnergy(ItemStack stack, int amount) {
		if (stack.getItem() instanceof ItemEnergyStorage && amount > 0) {
			LazyOptional<IEnergyStorage> optionalOxygenCap =
					stack.getCapability(ForgeCapabilities.ENERGY);
			if (!optionalOxygenCap.isPresent()) {
				return 0;
			}
			IEnergyStorage energyCap = optionalOxygenCap.orElseThrow(NullPointerException::new);
			return energyCap.extractEnergy(amount, false);
		}
		return 0;
	}

	public int getCapacity() {
		return this.config.capacity.get();
	}

	public int getMaxReceive() {
		return this.config.maxReceive.get();
	}

	public int getMaxExtract() {
		return this.config.maxExtract.get();
	}

	public int getEnergy() {
		return this.config.energy.get();
	}

}
