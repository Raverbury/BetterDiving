package meldexun.better_diving.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RavineOutcropFeature extends Feature<OceanOreFeatureConfig> {

    public RavineOutcropFeature(Codec<OceanOreFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<OceanOreFeatureConfig> featurePlaceContext) {
        OceanOreFeatureConfig config =
                featurePlaceContext.config();
        if (!config.getConfig().enabled.get()) {
            return false;
        }
        RandomSource rand = featurePlaceContext.random();
        if (config.getConfig().chance.get() > 0 && rand
                .nextInt(config.getConfig().chance.get()) != 0) {
            return false;
        }
        ChunkGenerator generator = featurePlaceContext.chunkGenerator();
        BlockPos pos = featurePlaceContext.origin();
        WorldGenLevel reader = featurePlaceContext.level();
        int i = 0;
        int j = config.sample(rand);

        for (int k = 0; k < j; k++) {
            for (int l = 0; l < 16; l++) {
                int x = rand.nextInt(8) - rand.nextInt(8);
                int z = rand.nextInt(8) - rand.nextInt(8);
                int height = generator.getBaseHeight(pos.getX() + x,
                        pos.getZ() + z, Heightmap.Types.OCEAN_FLOOR,
                        reader.getLevel().getChunkAt(pos)
                                .getHeightAccessorForGeneration(),
                        reader.getLevel().getChunkSource().randomState());
                if (height <= reader.getLevel().getMinBuildHeight() + 10) {
                    continue;
                }
                int y = 1 + rand.nextInt(height - 1);
                if (y < config.getConfig().minHeight.get()) {
                    continue;
                }
                if (y > config.getConfig().maxHeight.get()) {
                    continue;
                }
                BlockPos p = new BlockPos(pos.getX() + x, y, pos.getZ() + z);
                if (!reader.getBlockState(p).is(Blocks.WATER)) {
                    continue;
                }
                boolean flag = false;
                for (int x1 = -1; x1 <= 1; x1++) {
                    for (int z1 = -1; z1 <= 1; z1++) {
                        if (y > reader.getHeight(Heightmap.Types.OCEAN_FLOOR,
                                p.getX() + x1, p.getZ() + z1)) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                        break;
                    }
                }
                if (!flag) {
                    continue;
                }
                List<Direction> list = new ArrayList<>();
                list.add(Direction.UP);
                list.add(Direction.NORTH);
                list.add(Direction.SOUTH);
                list.add(Direction.EAST);
                list.add(Direction.WEST);
                Collections.shuffle(list);
                for (Direction d : list) {
                    BlockState state = config.getBlock().defaultBlockState()
                            .setValue(BlockStateProperties.FACING, d);
                    if (state.canSurvive(reader, p)) {
                        reader.setBlock(p, state, 2);
                        i++;
                        break;
                    }
                }
            }
        }
        return i > 0;
    }
}
