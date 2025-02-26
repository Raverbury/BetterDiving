package meldexun.better_diving.block;

import meldexun.better_diving.init.BetterDivingSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class BlockUnderwaterOre extends Block implements SimpleWaterloggedBlock {

    public static final IntegerProperty ROTATION = IntegerProperty.create(
            "rotated", 0,
            3);
    private static final Map<Direction, VoxelShape> SHAPES;

    static {
        Map<Direction, VoxelShape> m = new EnumMap<>(Direction.class);
        m.put(Direction.UP, Block.box(2.0D, 0.0D, 2.0D, 14.0D, 8.0D, 14.0D));
        m.put(Direction.DOWN, Block.box(2.0D, 8.0D, 2.0D, 14.0D, 16.0D, 14.0D));
        m.put(Direction.NORTH,
                Block.box(2.0D, 2.0D, 8.0D, 14.0D, 14.0D, 16.0D));
        m.put(Direction.SOUTH, Block.box(2.0D, 2.0D, 0.0D, 14.0D, 14.0D, 8.0D));
        m.put(Direction.EAST, Block.box(0.0D, 2.0D, 2.0D, 8.0D, 14.0D, 14.0D));
        m.put(Direction.WEST, Block.box(8.0D, 2.0D, 2.0D, 16.0D, 14.0D, 14.0D));
        SHAPES = Collections.unmodifiableMap(m);
    }

    public BlockUnderwaterOre() {
        super(Block.Properties.copy(Blocks.SAND).sound(BetterDivingSounds.OUTCROP_SOUNDS));
        this.registerDefaultState(this.stateDefinition.any().setValue(
                        BlockStateProperties.FACING,
                        Direction.UP).setValue(ROTATION, 0)
                .setValue(BlockStateProperties.WATERLOGGED, true));
    }

    @SuppressWarnings("deprecation")
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ?
                Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn,
                               BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(BlockStateProperties.FACING));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING, ROTATION,
                BlockStateProperties.WATERLOGGED);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn,
                              BlockPos pos) {
        Direction d = state.getValue(BlockStateProperties.FACING);
        BlockPos p = pos.relative(d.getOpposite());
        return worldIn.getBlockState(p).isFaceSturdy(worldIn, p, d);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = this.defaultBlockState();
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        for (Direction d : context.getNearestLookingDirections()) {
            state = state.setValue(BlockStateProperties.FACING,
                    d.getOpposite());
            if (this.canSurvive(state, world, pos)) {
                return state.setValue(ROTATION, world.random.nextInt(4));
            }
        }
        return null;
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing,
                                  BlockState facingState, LevelAccessor worldIn,
                                  BlockPos currentPos, BlockPos facingPos) {
        if (facing.getOpposite() == stateIn.getValue(
                BlockStateProperties.FACING) && !stateIn.canSurvive(worldIn,
                currentPos)) {
            return Blocks.AIR.defaultBlockState();
        }
        if (stateIn.getValue(BlockStateProperties.WATERLOGGED)) {
            worldIn.scheduleTick(currentPos, Fluids.WATER,
                            Fluids.WATER.getTickDelay(worldIn));
        }
        return stateIn;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(BlockStateProperties.FACING,
                rot.rotate(state.getValue(BlockStateProperties.FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.setValue(BlockStateProperties.FACING,
                mirrorIn.mirror(state.getValue(BlockStateProperties.FACING)));
    }

}
