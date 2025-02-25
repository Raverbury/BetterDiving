package meldexun.better_diving.event;

import meldexun.better_diving.BetterDiving;
import meldexun.better_diving.block.BlockUnderwaterOre;
import meldexun.better_diving.config.BetterDivingConfig;
import meldexun.better_diving.config.BetterDivingConfig.ServerConfig.Ores.OreConfig;
import meldexun.better_diving.init.BetterDivingBlocks;
import meldexun.better_diving.init.BetterDivingFeatures;
import meldexun.better_diving.world.gen.feature.OceanOreFeatureConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;

@Mod.EventBusSubscriber(modid = BetterDiving.MOD_ID)
public class FeatureEventHandler {

    private static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURE_DEFERRED_REGISTER = DeferredRegister.create(
            Registries.CONFIGURED_FEATURE, BetterDiving.MOD_ID);
    public static ConfiguredFeature<OceanOreFeatureConfig, ?> limestoneOutcrop;
    public static ConfiguredFeature<OceanOreFeatureConfig, ?> sandstoneOutcrop;
    public static ConfiguredFeature<OceanOreFeatureConfig, ?> shaleOutcrop;

    private static void register(String name,
                              Feature<OceanOreFeatureConfig> feature, BlockUnderwaterOre block, OreConfig config) {
        CONFIGURED_FEATURE_DEFERRED_REGISTER.register(name, () ->
                new ConfiguredFeature<>(feature,
                        new OceanOreFeatureConfig(block,
                                config))
        );
        // return Registry.register(Registries.CONFIGURED_FEATURE,
        //         new ResourceLocation(BetterDiving.MOD_ID, name));
        // TODO
        // feature.(new OceanOreFeatureConfig(block, config)));
    }

    public static void registerConfiguredFeatures() {
        register("limestone_outcrop",
                BetterDivingFeatures.OCEAN_FLOOR.get(),
                BetterDivingBlocks.LIMESTONE_OUTCROP.get(),
                BetterDivingConfig.SERVER_CONFIG.ores.limestone);
        register("sandstone_outcrop",
                BetterDivingFeatures.OCEAN_FLOOR.get(),
                BetterDivingBlocks.SANDSTONE_OUTCROP.get(),
                BetterDivingConfig.SERVER_CONFIG.ores.sandstone);
        register("shalestone_outcrop",
                BetterDivingFeatures.OCEAN_RAVINE.get(),
                BetterDivingBlocks.SHALE_OUTCROP.get(),
                BetterDivingConfig.SERVER_CONFIG.ores.shale);
    }

    // @SubscribeEvent
    // public static void onBiomeLoadingEvent(BiomeLo event) {
    //     // TODO add biome config
    //     if (event. () == Category.OCEAN){
    //         event.getGeneration()
    //                 .addFeature(Decoration.UNDERGROUND_ORES, limestoneOutcrop);
    //         event.getGeneration()
    //                 .addFeature(Decoration.UNDERGROUND_ORES, sandstoneOutcrop);
    //         event.getGeneration()
    //                 .addFeature(Decoration.UNDERGROUND_ORES, shaleOutcrop);
    //     }
    // }

}
