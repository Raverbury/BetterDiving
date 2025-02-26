package meldexun.better_diving.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import meldexun.better_diving.block.BlockUnderwaterOre;
import meldexun.better_diving.config.BetterDivingConfig.ServerConfig.Ores.OreConfig;
import meldexun.better_diving.init.BetterDivingBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class OceanOreFeatureConfig implements FeatureConfiguration {

    public static final Codec<OceanOreFeatureConfig> CODEC = RecordCodecBuilder.create(
            (p_225407_) -> {
                return p_225407_.group(
                                BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block")
                                        .flatXmap(
                                                OceanOreFeatureConfig::apply,
                                                DataResult::success)
                                        .orElse(BetterDivingBlocks.LIMESTONE_OUTCROP.get())
                                        .forGetter((p_225424_) -> {
                                            return p_225424_.block;
                                        }),
                                Codec.BOOL.fieldOf("enabled").orElse(true)
                                        .forGetter((p_225420_) -> {
                                            return p_225420_.enabled;
                                        }), Codec.intRange(0, 100).fieldOf(
                                        "chance").orElse(1).forGetter((p_225422_) -> {
                                    return p_225422_.chance;
                                }),
                                Codec.intRange(-1024, 1024).fieldOf(
                                                "min_amount").orElse(2)
                                        .forGetter((p_225422_) -> {
                                            return p_225422_.minAmount;
                                        }),
                                Codec.intRange(-1024, 1024).fieldOf(
                                                "max_amount").orElse(8)
                                        .forGetter((p_225422_) -> {
                                            return p_225422_.maxAmount;
                                        }),
                                Codec.intRange(-1024, 1024).fieldOf(
                                                "min_height").orElse(-60)
                                        .forGetter((p_225422_) -> {
                                            return p_225422_.minHeight;
                                        }),
                                Codec.intRange(-1024, 1024).fieldOf(
                                                "max_height").orElse(63)
                                        .forGetter((p_225422_) -> {
                                            return p_225422_.maxHeight;
                                        }))
                        .apply(p_225407_, OceanOreFeatureConfig::new);
            });
    private final BlockUnderwaterOre block;
    public final boolean enabled;
    public final int chance;
    public final int minAmount;
    public final int maxAmount;
    public final int minHeight;
    public final int maxHeight;

    public OceanOreFeatureConfig(BlockUnderwaterOre block, OreConfig config) {
        this.block = block;
        this.enabled = config.enabled.get();
        this.chance = config.chance.get();
        this.minAmount = config.minAmount.get();
        this.maxAmount = config.maxAmount.get();
        this.minHeight = config.minHeight.get();
        this.maxHeight = config.maxHeight.get();
    }

    public OceanOreFeatureConfig(BlockUnderwaterOre block, boolean enabled,
                                 int chance,
                                 int minAmount, int maxAmount, int minHeight,
                                 int maxHeight) {
        this.block = block;
        this.enabled = enabled;
        this.chance = chance;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
    }

    private static DataResult<BlockUnderwaterOre> apply(Block p_225405_) {
        DataResult<BlockUnderwaterOre> dataresult;
        if (p_225405_ instanceof BlockUnderwaterOre blockUnderwaterOre) {
            dataresult = DataResult.success(blockUnderwaterOre);
        } else {
            dataresult = DataResult.error(() -> {
                return "Should be a BlockUnderwaterOre";
            });
        }

        return dataresult;
    }

    public BlockUnderwaterOre getBlock() {
        return block;
    }

    public int getRandomAmount(RandomSource rand) {
        int min = this.minAmount;
        int max = this.maxAmount;
        return min >= max ? min : min + rand.nextInt(max - min + 1);
    }
}
