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

import java.util.List;

public class RavineOutcropFeature extends Feature<OceanOreFeatureConfig> {

    public RavineOutcropFeature(Codec<OceanOreFeatureConfig> codec) {
        super(codec);
    }

    /**
     * Pick a random block, check 6 neighboring blocks for an attachable
     * surface, then check water/air
     *
     * @param featurePlaceContext
     * @return
     */
    @Override
    public boolean place(FeaturePlaceContext<OceanOreFeatureConfig> featurePlaceContext) {
        OceanOreFeatureConfig config =
                featurePlaceContext.config();
        if (!config.enabled) {
            return false;
        }
        RandomSource rand = featurePlaceContext.random();
        if (config.chance > 0 && rand
                .nextInt(config.chance) != 0) {
            return false;
        }
        ChunkGenerator generator = featurePlaceContext.chunkGenerator();
        BlockPos pos = featurePlaceContext.origin();
        WorldGenLevel reader = featurePlaceContext.level();
        int i = 0;
        int j = config.getRandomAmount(rand);

        for (int k = 0; k < j; k++) {
            int x = rand.nextInt(8) - rand.nextInt(8);
            int z = rand.nextInt(8) - rand.nextInt(8);
            int yMax = reader.getHeight(Heightmap.Types.OCEAN_FLOOR,
                    pos.getX() + x, pos.getZ() + z) - 1;
            yMax = Math.min(yMax, config.maxHeight);
            int yMin = Math.max(reader.getLevel().getMinBuildHeight() + 10,
                    config.minHeight);
            int y = yMax > yMin ? yMin + rand.nextInt(yMax - yMin) : yMin;

            BlockPos p = new BlockPos(pos.getX() + x, y, pos.getZ() + z);
            List<Direction> directions = List.of(Direction.UP,
                    Direction.SOUTH, Direction.NORTH, Direction.WEST,
                    Direction.EAST, Direction.DOWN);
            for (Direction attachDirection : directions) {
                Direction facingDirection = attachDirection.getOpposite();
                BlockPos attachTo = p.relative(attachDirection);
                BlockState state = config.getBlock().defaultBlockState()
                        .setValue(
                                BlockStateProperties.FACING,
                                facingDirection);
                if (reader.getBlockState(attachTo).isFaceSturdy(reader,
                        attachTo, facingDirection) && state.canSurvive(
                        reader, p)) {
                    // when spawning in water, always succeed, else if
                    // air then has 1/3 chance to succeed
                    BlockState spawnBlockState = reader.getBlockState(p);
                    boolean shouldSpawn = spawnBlockState
                            .is(Blocks.WATER);
                    boolean waterlogged = true;
                    if (!shouldSpawn) {
                        if (spawnBlockState.isAir()) {
                            if (rand.nextInt(3) == 0) {
                                shouldSpawn = true;
                                waterlogged = false;
                            }
                        }
                    }
                    if (shouldSpawn) {
                        state =
                                state.setValue(
                                        BlockStateProperties.WATERLOGGED,
                                        waterlogged);
                        reader.setBlock(p, state, 2);
                        i++;
                        break;
                    }
                }
            }
        }
        return i > 0;
    }

    // @Override
    // public boolean place(FeaturePlaceContext<OceanOreFeatureConfig> featurePlaceContext) {
    //     OceanOreFeatureConfig config =
    //             featurePlaceContext.config();
    //     if (!config.enabled) {
    //         return false;
    //     }
    //     RandomSource rand = featurePlaceContext.random();
    //     if (config.chance > 0 && rand
    //             .nextInt(config.chance) != 0) {
    //         return false;
    //     }
    //     ChunkGenerator generator = featurePlaceContext.chunkGenerator();
    //     BlockPos pos = featurePlaceContext.origin();
    //     WorldGenLevel reader = featurePlaceContext.level();
    //     int i = 0;
    //     int j = config.getRandomAmount(rand);
    //
    //     for (int k = 0; k < j; k++) {
    //         for (int l = 0; l < 2; l++) {
    //             int x = rand.nextInt(8) - rand.nextInt(8);
    //             int z = rand.nextInt(8) - rand.nextInt(8);
    //             int height = generator.getBaseHeight(pos.getX() + x,
    //                     pos.getZ() + z, Heightmap.Types.OCEAN_FLOOR,
    //                     reader.getLevel().getChunkAt(pos)
    //                             .getHeightAccessorForGeneration(),
    //                     reader.getLevel().getChunkSource().randomState());
    //             if (height <= reader.getLevel().getMinBuildHeight() + 10) {
    //                 continue;
    //             }
    //             int y = 1 + rand.nextInt(height - 1);
    //             if (y < config.minHeight) {
    //                 continue;
    //             }
    //             if (y > config.maxHeight) {
    //                 continue;
    //             }
    //             BlockPos p = new BlockPos(pos.getX() + x, y, pos.getZ() + z);
    //             if (!reader.getBlockState(p).is(Blocks.WATER)) {
    //                 continue;
    //             }
    //             boolean flag = false;
    //             for (int x1 = -1; x1 <= 1; x1++) {
    //                 for (int z1 = -1; z1 <= 1; z1++) {
    //                     if (y > reader.getHeight(Heightmap.Types.OCEAN_FLOOR,
    //                             p.getX() + x1, p.getZ() + z1)) {
    //                         flag = true;
    //                         break;
    //                     }
    //                 }
    //                 if (flag) {
    //                     break;
    //                 }
    //             }
    //             if (!flag) {
    //                 continue;
    //             }
    //             List<Direction> list = new ArrayList<>();
    //             list.add(Direction.UP);
    //             list.add(Direction.NORTH);
    //             list.add(Direction.SOUTH);
    //             list.add(Direction.EAST);
    //             list.add(Direction.WEST);
    //             Collections.shuffle(list);
    //             for (Direction d : list) {
    //                 BlockState state = config.getBlock().defaultBlockState()
    //                         .setValue(BlockStateProperties.FACING, d);
    //                 if (reader.getBlockState(p)
    //                         .is(Blocks.WATER) && state.canSurvive(reader, p)) {
    //                     reader.setBlock(p, state, 2);
    //                     i++;
    //                     break;
    //                 }
    //             }
    //         }
    //     }
    //     return i > 0;
    // }
}
