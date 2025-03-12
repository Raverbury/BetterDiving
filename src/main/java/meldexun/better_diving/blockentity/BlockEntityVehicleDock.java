package meldexun.better_diving.blockentity;

import meldexun.better_diving.BetterDiving;
import meldexun.better_diving.api.entity.EntityBetterDivingVehicle;
import meldexun.better_diving.config.BetterDivingConfig;
import meldexun.better_diving.init.BetterDivingBlockEntities;
import meldexun.better_diving.network.packet.server.SPacketUndockVehicle;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlockEntityVehicleDock extends BlockEntity {

    private EntityBetterDivingVehicle dockingVehicle = null;

    private LazyOptional<IEnergyStorage> ownEnergyCap = null;

    public BlockEntityVehicleDock(BlockPos p_155229_, BlockState p_155230_) {
        super(BetterDivingBlockEntities.DOCKING_BE.get(), p_155229_, p_155230_);
    }

    public static void commonTick(Level level, BlockPos pos, BlockState state, BlockEntityVehicleDock blockEntity) {
        // recharge solar
        if (!level.isClientSide() && level.getGameTime() % 40 == 0) {
            if (blockEntity.ownEnergyCap == null) {
                blockEntity.ownEnergyCap = blockEntity.getCapability(
                        ForgeCapabilities.ENERGY);
                blockEntity.ownEnergyCap.addListener((c) -> {
                    blockEntity.ownEnergyCap = null;
                });
            }
            if (blockEntity.ownEnergyCap != null) {
                blockEntity.ownEnergyCap.ifPresent((c) -> {
                    if (level.canSeeSky(pos.above())) {
                        level.updateSkyBrightness();
                        if (level.isDay()) {
                            c.receiveEnergy(
                                    BetterDivingConfig.SERVER_CONFIG.dockingBlock.dockBlockSolarGenerationRate.get(),
                                    false);
                        }
                    }
                });
            }
        }

        // find vehicle, equivalent to old stage 0
        if (!blockEntity.verifyDockingVehicle()) {
            blockEntity.dockingVehicle = getFirstNearbyVehicle(
                    level, pos);
            if (blockEntity.dockingVehicle != null) {
                blockEntity.dockingVehicle.startDocking();
            }
        }

        // dock, old stage 1
        if (blockEntity.dockingVehicle != null && blockEntity.dockingVehicle.isDocked()) {
            double dockX = pos.getX() + 0.5;
            double dockY = pos.getY() - 3.5 - blockEntity.dockingVehicle.getBoundingBox()
                    .getYsize();
            double dockZ = pos.getZ() + 0.5;
            blockEntity.dockingVehicle.setDeltaMovement(Vec3.ZERO);

            float curretYaw = blockEntity.dockingVehicle.getYRot();
            float yawDiff = getYawTarget(
                    blockEntity.dockingVehicle) - curretYaw;
            if (Math.abs(yawDiff) > 2f) {
                yawDiff *= 0.14f;
            }
            blockEntity.dockingVehicle.setYRot(
                    curretYaw + yawDiff);

            float curretPitch = blockEntity.dockingVehicle.getXRot();
            float pitchDiff = -curretPitch;
            if (Math.abs(pitchDiff) > 1f) {
                pitchDiff *= 0.2f;
            }
            blockEntity.dockingVehicle.setXRot(
                    curretPitch + pitchDiff);

            double diffX = dockX - blockEntity.dockingVehicle.getX();
            double diffY = dockY - blockEntity.dockingVehicle.getY();
            double diffZ = dockZ - blockEntity.dockingVehicle.getZ();

            double sqrDist = diffX * diffX + diffY * diffY + diffZ * diffZ;

            // double scale = 2d / sqrDist;
            double scale = 0.06d;

            // old stage 2 but not really
            if (sqrDist <= 0.005d) {
                blockEntity.dockingVehicle.setPos(dockX, dockY, dockZ);
                if (blockEntity.dockingVehicle.hasControllingPassenger()) {
                    blockEntity.dockingVehicle.getControllingPassenger()
                            .dismountTo(
                                    dockX,
                                    dockY + 0.25d + blockEntity.dockingVehicle.getBoundingBox()
                                            .getYsize(),
                                    dockZ
                            );
                }
                if (!level.isClientSide()) {
                    int missingEnergy =
                            blockEntity.dockingVehicle.getEnergyCapacity() - blockEntity.dockingVehicle.getEnergy();
                    if (missingEnergy > 0) {
                        if (blockEntity.ownEnergyCap != null) {
                            blockEntity.ownEnergyCap.ifPresent((c) -> {
                                int transferAmount = c.extractEnergy(
                                        BetterDivingConfig.SERVER_CONFIG.dockingBlock.dockBlockEnergyTransferRate.get(),
                                        false);
                                blockEntity.dockingVehicle.receiveEnergy(
                                        transferAmount);
                            });
                        }
                    }
                }
            } else {
                blockEntity.dockingVehicle.setPos(
                        blockEntity.dockingVehicle.getX() + diffX * scale,
                        blockEntity.dockingVehicle.getY() + diffY * scale,
                        blockEntity.dockingVehicle.getZ() + diffZ * scale);
            }
            // old stage 3
        } else if (blockEntity.dockingVehicle != null) {
            if (getNearbyVehicle(level, pos).isEmpty()) {
                blockEntity.dockingVehicle = null;
            }
        }

        level.updateNeighbourForOutputSignal(pos, state.getBlock());
    }

    private static List<EntityBetterDivingVehicle> getNearbyVehicle(Level level,
                                                                    BlockPos blockPos) {
        return level.getEntitiesOfClass(
                EntityBetterDivingVehicle.class,
                new AABB(blockPos.getX(), blockPos.getY() - 6,
                        blockPos.getZ(), blockPos.getX() + 1,
                        blockPos.getY(), blockPos.getZ() + 1),
                EntitySelector.ENTITY_STILL_ALIVE);
    }

    private static EntityBetterDivingVehicle getFirstNearbyVehicle(Level level, BlockPos blockPos) {
        List<EntityBetterDivingVehicle> vehicles = getNearbyVehicle(level
                , blockPos);
        if (vehicles.isEmpty()) {
            return null;
        }
        for (EntityBetterDivingVehicle vehicle : vehicles) {
            if (!vehicle.isDocked()) {
                return vehicle;
            }
        }
        return null;
    }

    // block is direction less, so be needs to figure out where the vehicle
    // should face based on its current yaw
    private static int getYawTarget(EntityBetterDivingVehicle vehicle) {
        float yaw = vehicle.getYRot();
        float rem = yaw % 90;
        float targetYaw = Math.abs(rem) < 45f ? yaw - rem : rem < 0 ?
                yaw + (-90f - rem) : yaw + (90f - rem);
        return (int) targetYaw;
    }

    public static CompoundTag save(CompoundTag nbt, BlockEntityVehicleDock blockEntityVehicleDock) {
        if (nbt == null) {
            nbt = new CompoundTag();
        }
        return nbt;
    }

    public static void load(CompoundTag nbt, BlockEntityVehicleDock blockEntityVehicleDock) {
    }

    private boolean verifyDockingVehicle() {
        if (this.dockingVehicle == null) {
            return false;
        }

        if (this.dockingVehicle.isRemoved()) {
            return false;
        }

        return true;
    }

    /**
     * This almost certainly runs only on server
     * through BlockVehicleBlock#onRemove,
     * so we need to sync undock to client
     */
    public void serverReleaseVehicleAndForge() {
        if (this.dockingVehicle != null) {
            this.dockingVehicle.releaseDocking();
                BetterDiving.NETWORK.send(
                        PacketDistributor.TRACKING_ENTITY.with(() -> this.dockingVehicle),
                        new SPacketUndockVehicle(this.dockingVehicle));
            this.dockingVehicle = null;
        }
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        save(nbt, this);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        load(nbt, this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        return save(nbt, this);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        load(tag, this);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    /**
     * 0 if no vehicle, 1 to 15 with vehicle depending on energy fullness
     *
     * @return
     */
    public int getRedstoneSignal() {
        if (this.dockingVehicle == null || !this.dockingVehicle.isDocked()) {
            return 0;
        }
        int energyCap = this.dockingVehicle.getEnergyCapacity();
        if (energyCap == 0) {
            return 15;
        }
        return 1 + (int) (14f * this.dockingVehicle.getEnergy() / this.dockingVehicle.getEnergyCapacity());
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        if (ownEnergyCap != null) {
            ownEnergyCap.invalidate();
        }
    }
}
