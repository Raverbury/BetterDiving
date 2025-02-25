package meldexun.better_diving.biome;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

public record OceanBiomeModifier() implements BiomeModifier {
    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD && biome.is(BiomeTags.IS_OCEAN)) {
            // builder.getGenerationSettings().addFeature(
            //         GenerationStep.Decoration.UNDERGROUND_ORES,
            //         FeatureEventHandler.limestoneOutcrop.);
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return null;
    }
}
