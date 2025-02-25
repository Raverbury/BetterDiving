package meldexun.better_diving.oxygenprovider;

import java.util.EnumSet;
import java.util.Set;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;

public abstract class AbstractDivingGear {

	public final ResourceLocation registryName;
	public final Set<EquipmentSlot> slots;

	public AbstractDivingGear(ResourceLocation registryName, Set<EquipmentSlot> slots) {
		this.registryName = registryName;
		this.slots = EnumSet.copyOf(slots);
	}

	public boolean isValidSlot(EquipmentSlot slot) {
		return this.slots.contains(slot);
	}

	protected static Set<EquipmentSlot> parseValidSlots(String[] configArray, int offset) {
		Set<EquipmentSlot> slots = EnumSet.noneOf(EquipmentSlot.class);
		for (int i = 0; i < EquipmentSlot.values().length; i++) {
			if (Boolean.parseBoolean(configArray[i + offset])) {
				slots.add(EquipmentSlot.values()[i]);
			}
		}
		return slots;
	}

}
