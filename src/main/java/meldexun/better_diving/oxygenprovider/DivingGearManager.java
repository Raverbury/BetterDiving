package meldexun.better_diving.oxygenprovider;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import meldexun.better_diving.config.BetterDivingConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class DivingGearManager {

	private static final Map<ResourceLocation, OxygenProviderEntity> oxygenProviderEntityMap = new HashMap<>();
	private static final Map<ResourceLocation, DivingMaskProviderItem> divingMaskProviderItemMap = new HashMap<>();
	private static final Map<ResourceLocation, OxygenProviderItem> oxygenProviderItemMap = new HashMap<>();
	private static final Map<ResourceLocation, MiningspeedProviderItem> miningspeedProviderItemMap = new HashMap<>();
	private static final Map<ResourceLocation, SwimspeedProviderItem> swimspeedProviderItemMap = new HashMap<>();

	public static void reloadConfigs() {
		oxygenProviderEntityMap.clear();
		// for (String s : BetterDivingConfig.SERVER_CONFIG.oxygenProviderEntities.get()) {
		// 	OxygenProviderEntity oxygenProviderEntity = OxygenProviderEntity.parse(s);
		// 	if (oxygenProviderEntity != null) {
		// 		oxygenProviderEntityMap.computeIfAbsent(oxygenProviderEntity.registryName, key -> oxygenProviderEntity);
		// 	}
		// }
		//
		// divingMaskProviderItemMap.clear();
		// for (String s : BetterDivingConfig.SERVER_CONFIG.divingMaskProviderItems.get()) {
		// 	DivingMaskProviderItem divingMaskProviderItem = DivingMaskProviderItem.parse(s);
		// 	if (divingMaskProviderItem != null) {
		// 		divingMaskProviderItemMap.computeIfAbsent(divingMaskProviderItem.registryName, key -> divingMaskProviderItem);
		// 	}
		// }
		//
		// oxygenProviderItemMap.clear();
		// for (String s : BetterDivingConfig.SERVER_CONFIG.oxygenProviderItems.get()) {
		// 	OxygenProviderItem oxygenProviderItem = OxygenProviderItem.parse(s);
		// 	if (oxygenProviderItem != null) {
		// 		oxygenProviderItemMap.computeIfAbsent(oxygenProviderItem.registryName, key -> oxygenProviderItem);
		// 	}
		// }
		//
		// miningspeedProviderItemMap.clear();
		// for (String s : BetterDivingConfig.SERVER_CONFIG.miningspeedProviderItems.get()) {
		// 	MiningspeedProviderItem miningspeedProviderItem = MiningspeedProviderItem.parse(s);
		// 	if (miningspeedProviderItem != null) {
		// 		miningspeedProviderItemMap.computeIfAbsent(miningspeedProviderItem.registryName, key -> miningspeedProviderItem);
		// 	}
		// }
		//
		// swimspeedProviderItemMap.clear();
		// for (String s : BetterDivingConfig.SERVER_CONFIG.swimspeedProviderItems.get()) {
		// 	SwimspeedProviderItem swimspeedProviderItem = SwimspeedProviderItem.parse(s);
		// 	if (swimspeedProviderItem != null) {
		// 		swimspeedProviderItemMap.computeIfAbsent(swimspeedProviderItem.registryName, key -> swimspeedProviderItem);
		// 	}
		// }
	}

	@Nullable
	public static OxygenProviderEntity getOxygenProviderEntity(Entity entity) {
		return oxygenProviderEntityMap.get(ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()));
	}

	@Nullable
	public static DivingMaskProviderItem getDivingMaskProviderItem(ItemStack stack) {
		if (stack.isEmpty()) {
			return null;
		}
		return divingMaskProviderItemMap.get(ForgeRegistries.ITEMS.getKey(stack.getItem()));
	}

	@Nullable
	public static OxygenProviderItem getOxygenProviderItem(ItemStack stack) {
		if (stack.isEmpty()) {
			return null;
		}
		return oxygenProviderItemMap.get(ForgeRegistries.ITEMS.getKey(stack.getItem()));
	}

	@Nullable
	public static MiningspeedProviderItem getMiningspeedProviderItem(ItemStack stack) {
		if (stack.isEmpty()) {
			return null;
		}
		return miningspeedProviderItemMap.get(ForgeRegistries.ITEMS.getKey(stack.getItem()));
	}

	@Nullable
	public static SwimspeedProviderItem getSwimspeedProviderItem(ItemStack stack) {
		if (stack.isEmpty()) {
			return null;
		}
		return swimspeedProviderItemMap.get(ForgeRegistries.ITEMS.getKey(stack.getItem()));
	}

}
