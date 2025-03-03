package meldexun.better_diving.init;

import meldexun.better_diving.BetterDiving;
import meldexun.better_diving.blockentity.BlockEntityVehicleDock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BetterDivingBlockEntities {

    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES,
                    BetterDiving.MOD_ID);

    public static final RegistryObject<BlockEntityType<BlockEntityVehicleDock>> DOCKING_BE =
            BLOCK_ENTITY_TYPES.register("docking_be",
                    () -> BlockEntityType.Builder.of(
                            BlockEntityVehicleDock::new,
                            BetterDivingBlocks.DOCKING_BLOCK.get()
                    ).build(null));

    public static void registerBlockEntities() {
        BLOCK_ENTITY_TYPES.register(
                FMLJavaModLoadingContext.get().getModEventBus());
    }
}
