package meldexun.better_diving.init;

import meldexun.better_diving.BetterDiving;
import meldexun.better_diving.inventory.container.ContainerSeamothEntity;
import meldexun.better_diving.inventory.container.ContainerSeamothItem;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BetterDivingContainers {

	private static final DeferredRegister<MenuType<?>> CONTAINERS =
			DeferredRegister.create(ForgeRegistries.MENU_TYPES, BetterDiving.MOD_ID);

	public static final RegistryObject<MenuType<ContainerSeamothEntity>> SEAMOTH_ENTITY = CONTAINERS.register("seamoth_entity", () -> new MenuType<>(ContainerSeamothEntity::new,
			FeatureFlags.DEFAULT_FLAGS));
	public static final RegistryObject<MenuType<ContainerSeamothItem>> SEAMOTH_ITEM = CONTAINERS.register("seamoth_item", () -> new MenuType<>(ContainerSeamothItem::new, FeatureFlags.DEFAULT_FLAGS));

	public static void registerContainers() {
		CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

}
