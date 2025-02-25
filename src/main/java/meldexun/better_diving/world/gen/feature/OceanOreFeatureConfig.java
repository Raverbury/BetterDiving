package meldexun.better_diving.world.gen.feature;

import java.util.Random;

import com.mojang.serialization.Codec;

import meldexun.better_diving.block.BlockUnderwaterOre;
import meldexun.better_diving.config.BetterDivingConfig.ServerConfig.Ores.OreConfig;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class OceanOreFeatureConfig implements FeatureConfiguration {

	public static final Codec<OceanOreFeatureConfig> CODEC = Codec.unit(() -> null);
	private final BlockUnderwaterOre block;
	private final OreConfig config;

	public OceanOreFeatureConfig(BlockUnderwaterOre block, OreConfig config) {
		this.block = block;
		this.config = config;
	}

	public BlockUnderwaterOre getBlock() {
		return block;
	}

	public OreConfig getConfig() {
		return config;
	}

	public int sample(RandomSource rand) {
		int min = config.minAmount.get();
		int max = config.maxAmount.get();
		return min >= max ? min : min + rand.nextInt(max - min + 1);
	}

}
