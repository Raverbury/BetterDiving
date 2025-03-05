package meldexun.better_diving.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.ArrayList;
import java.util.List;

public class SeafloorOutcropFeature extends Feature<OceanOreFeatureConfig> {

    public SeafloorOutcropFeature(Codec<OceanOreFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<OceanOreFeatureConfig> featurePlaceContext) {
        OceanOreFeatureConfig config =
                featurePlaceContext.config();
        if (!config.enabled) {
            return false;
        }
        RandomSource rand = featurePlaceContext.random();
        if (config.chance > 0 && rand.nextInt(
                config.chance) != 0) {
            return false;
        }
        BlockPos pos = featurePlaceContext.origin();
        WorldGenLevel reader = featurePlaceContext.level();
        int i = 0;
        int j = config.getRandomAmount(rand);

        for (int k = 0; k < j; k++) {
            int x = rand.nextInt(8) - rand.nextInt(8);
            int z = rand.nextInt(8) - rand.nextInt(8);
            int yCenter = reader.getHeight(Heightmap.Types.OCEAN_FLOOR,
                    pos.getX() + x, pos.getZ() + z);
            if (yCenter > config.maxHeight) {
                continue;
            }
            int yNorth = reader.getHeight(Heightmap.Types.OCEAN_FLOOR,
                    pos.getX() + x, pos.getZ() + z - 1);
            int ySouth = reader.getHeight(Heightmap.Types.OCEAN_FLOOR,
                    pos.getX() + x, pos.getZ() + z + 1);
            int yEast = reader.getHeight(Heightmap.Types.OCEAN_FLOOR,
                    pos.getX() + x + 1, pos.getZ() + z);
            int yWest = reader.getHeight(Heightmap.Types.OCEAN_FLOOR,
                    pos.getX() + x - 1, pos.getZ() + z);
            int yMax = Math.max(Math.max(yNorth, ySouth),
                    Math.max(yEast, yWest));
            if (yMax < config.minHeight) {
                continue;
            }
            int yMin = Math.max(yCenter,
                    config.minHeight);
            yMax = Math.min(yMax, config.maxHeight);
            int y = yMax > yMin ? yMin + rand.nextInt(yMax - yMin) : yMin;

            BlockPos p = new BlockPos(pos.getX() + x, y, pos.getZ() + z);
            List<Direction> list = new ArrayList<>();
            if (y == yCenter) {
                list.add(Direction.UP);
            }
            if (y < yNorth) {
                list.add(Direction.SOUTH);
            }
            if (y < ySouth) {
                list.add(Direction.NORTH);
            }
            if (y < yEast) {
                list.add(Direction.WEST);
            }
            if (y < yWest) {
                list.add(Direction.EAST);
            }
            Direction dir = list.get(rand.nextInt(list.size()));
            BlockState state = config.getBlock().defaultBlockState()
                    .setValue(
                            BlockStateProperties.FACING, dir);
            if (reader.getBlockState(p)
                    .is(Blocks.WATER) && state.canSurvive(reader, p)) {
                reader.setBlock(p, state, 2);
                i++;
                break;
            }
        }
        return i > 0;
    }

}
