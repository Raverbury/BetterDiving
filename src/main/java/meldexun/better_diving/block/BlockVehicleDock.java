package meldexun.better_diving.block;

import meldexun.better_diving.blockentity.BlockEntityVehicleDock;
import meldexun.better_diving.init.BetterDivingBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BlockVehicleDock extends BaseEntityBlock {

    public BlockVehicleDock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockEntityVehicleDock(blockPos, blockState);
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level,
                                                                  BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClientSide()? null : createTickerHelper(blockEntityType,
                BetterDivingBlockEntities.DOCKING_BE.get(),
                BlockEntityVehicleDock::serverTick);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState p_60457_) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level,
                                     BlockPos blockPos) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof BlockEntityVehicleDock) {
            return ((BlockEntityVehicleDock) blockEntity).getRedstoneSignal();
        }
        return 0;
    }

    @Override
    public void onRemove(BlockState p_60515_, Level level, BlockPos blockPos,
                         BlockState p_60518_, boolean p_60519_) {
        // this is called on server only, so we need way to sync sigh
        // syncedentitydata it is
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof BlockEntityVehicleDock) {
            ((BlockEntityVehicleDock) blockEntity).releaseVehicle();
        }
        super.onRemove(p_60515_, level, blockPos, p_60518_, p_60519_);
    }
}
