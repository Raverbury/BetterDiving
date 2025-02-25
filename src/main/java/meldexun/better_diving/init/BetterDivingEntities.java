package meldexun.better_diving.init;

import meldexun.better_diving.BetterDiving;
import meldexun.better_diving.entity.EntitySeamoth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BetterDivingEntities {

	private static final DeferredRegister<EntityType<?>> ENTITIES =
			DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, BetterDiving.MOD_ID);

	public static final RegistryObject<EntityType<EntitySeamoth>> SEAMOTH =
			ENTITIES.register("seamoth",
					() -> EntityType.Builder.of(EntitySeamoth::new, MobCategory.MISC).fireImmune().setTrackingRange(64).setUpdateInterval(1).sized(1.82F, 1.82F).setShouldReceiveVelocityUpdates(true).build("seamoth"));

	public static void registerEntities() {
		ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}
