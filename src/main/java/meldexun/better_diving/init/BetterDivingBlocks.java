package meldexun.better_diving.init;

import meldexun.better_diving.BetterDiving;
import meldexun.better_diving.block.BlockUnderwaterOre;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BetterDivingBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS,
                    BetterDiving.MOD_ID);

    public static final RegistryObject<BlockUnderwaterOre> LIMESTONE_OUTCROP
            = BLOCKS.register("limestone_outcrop", BlockUnderwaterOre::new);
    public static final RegistryObject<BlockUnderwaterOre> SANDSTONE_OUTCROP = BLOCKS.register(
            "sandstone_outcrop", BlockUnderwaterOre::new);
    public static final RegistryObject<BlockUnderwaterOre> SHALE_OUTCROP = BLOCKS.register(
            "shale_outcrop", BlockUnderwaterOre::new);

    public static final RegistryObject<Block> TITANIUM_BLOCK = BLOCKS.register(
            "titanium_block", () -> new Block(
                    BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<Block> SILVER_BLOCK = BLOCKS.register(
            "silver_block", () -> new Block(
                    BlockBehaviour.Properties.copy(
                            Blocks.IRON_BLOCK)));
    public static final RegistryObject<Block> LEAD_BLOCK = BLOCKS.register(
            "lead_block", () -> new Block(
                    BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<Block> LITHIUM_BLOCK = BLOCKS.register(
            "lithium_block", () -> new Block(
                    BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));

    public static void registerBlocks() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

}
