package meldexun.better_diving.blockentity;

import meldexun.better_diving.api.entity.EntityBetterDivingVehicle;
import meldexun.better_diving.init.BetterDivingBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class BlockEntityVehicleDock extends BlockEntity {

    private int dockingStage = 0;
    private int dockProgress = 0;
    private UUID vehicleUuid = null;
    private boolean hasLoaded = false;
    private EntityBetterDivingVehicle dockingVehicle = null;

    public BlockEntityVehicleDock(BlockPos p_155229_, BlockState p_155230_) {
        super(BetterDivingBlockEntities.DOCKING_BE.get(), p_155229_, p_155230_);
    }

    public static void commonTick(Level level, BlockPos pos, BlockState state, BlockEntityVehicleDock blockEntity) {
        if (!blockEntity.hasLoaded) {
            blockEntity.hasLoaded = true;
            if (blockEntity.vehicleUuid != null) {
                if (level instanceof ServerLevel) {
                    ServerLevel serverLevel = (ServerLevel) level;
                    Entity entity =
                            serverLevel.getEntity(blockEntity.vehicleUuid);
                    if (entity instanceof EntityBetterDivingVehicle) {
                        blockEntity.dockingVehicle = (EntityBetterDivingVehicle) entity;
                    }
                } else {
                    UUID uuid = blockEntity.vehicleUuid;
                    for (EntityBetterDivingVehicle vehicle :
                            getNearbyVehicle(level,
                                    pos)) {
                        if (vehicle.getUUID().equals(uuid)) {
                            blockEntity.dockingVehicle = vehicle;
                            break;
                        }
                    }
                }
            }
        }
        switch (blockEntity.dockingStage) {
            case 0:
                EntityBetterDivingVehicle vehicle = getFirstNearbyVehicle(
                        level, pos);
                if (vehicle != null) {
                    blockEntity.dockingVehicle = vehicle;
                    blockEntity.dockingStage = 1;
                    if (!level.isClientSide()) {
                        blockEntity.setChanged();
                    }
                }
                break;
            case 1:
                if (blockEntity.verifyDockingVehicle()) {
                    Vec3 dockPos = new Vec3(pos.getX() + 0.5,
                            pos.getY() - 3.5 - blockEntity.dockingVehicle.getBoundingBox()
                                    .getYsize(),
                            pos.getZ() + 0.5);
                    blockEntity.dockingVehicle.startDocking();
                    blockEntity.dockingVehicle.setDeltaMovement(Vec3.ZERO);
                    blockEntity.dockProgress += 1;

                    float yaw = blockEntity.dockingVehicle.getYRot();
                    blockEntity.dockingVehicle.setYRot(
                            Mth.lerpInt(blockEntity.dockProgress / 30f,
                                    (int) yaw,
                                    getYawTarget(blockEntity.dockingVehicle)));

                    float pitch = blockEntity.dockingVehicle.getXRot();
                    blockEntity.dockingVehicle.setXRot(
                            Mth.lerpInt(blockEntity.dockProgress / 25f,
                                    (int) pitch,
                                    0));

                    Vec3 vehiclePos = blockEntity.dockingVehicle.position();
                    double newX = Mth.lerp(blockEntity.dockProgress / 45.0,
                            vehiclePos.x, dockPos.x);
                    double yLerpScale = 0.05;
                    if (blockEntity.dockProgress > 25) {
                        yLerpScale = 0.4;
                    }
                    double newY =
                            Mth.lerp(
                                    yLerpScale * blockEntity.dockProgress / 45.0f,
                                    vehiclePos.y, dockPos.y);
                    double newZ = Mth.lerp(blockEntity.dockProgress / 45.0,
                            vehiclePos.z, dockPos.z);
                    Vec3 newVehiclePos = new Vec3(newX, newY, newZ);
                    blockEntity.dockingVehicle.setPos(newVehiclePos);
                    blockEntity.dockingVehicle.hasImpulse = true;

                    if (blockEntity.dockProgress >= 50) {
                        blockEntity.dockingVehicle.setPos(dockPos);
                        if (blockEntity.dockProgress >= 60) {
                            blockEntity.dockingVehicle.shouldUndock
                                    = false;
                            blockEntity.dockingStage = 2;
                            if (blockEntity.dockingVehicle.hasControllingPassenger()) {
                                blockEntity.dockingVehicle.getControllingPassenger()
                                        .stopRiding();
                            }
                        }
                    }
                } else {
                    blockEntity.releaseVehicle();
                }
                if (!level.isClientSide()) {
                    blockEntity.setChanged();
                }
                break;
            case 2:
                if (blockEntity.verifyDockingVehicle()) {
                    Vec3 dockPos = new Vec3(pos.getX() + 0.5,
                            pos.getY() - 3.5 - blockEntity.dockingVehicle.getBoundingBox()
                                    .getYsize(),
                            pos.getZ() + 0.5);
                    blockEntity.dockingVehicle.setPos(dockPos);
                    blockEntity.dockingVehicle.setDeltaMovement(Vec3.ZERO);
                    blockEntity.dockingVehicle.hasImpulse = true;
                    int missingEnergy =
                            blockEntity.dockingVehicle.getEnergyCapacity() - blockEntity.dockingVehicle.getEnergy();
                    blockEntity.dockingVehicle.receiveEnergy(
                            Math.min(missingEnergy, 100));
                    if (blockEntity.dockingVehicle.shouldUndock) {
                        blockEntity.dockProgress -= 5;
                        if (blockEntity.dockProgress <= 0) {
                            blockEntity.dockingVehicle.releaseDocking();
                            blockEntity.dockingVehicle = null;
                            blockEntity.dockingStage = 3;
                        }
                        if (!level.isClientSide()) {
                            blockEntity.setChanged();
                        }
                    }
                } else {
                    blockEntity.releaseVehicle();
                    if (!level.isClientSide()) {
                        blockEntity.setChanged();
                    }
                }
                break;
            case 3:
                List<EntityBetterDivingVehicle> vehicles = getNearbyVehicle(
                        level, pos);
                if (vehicles.isEmpty()) {
                    blockEntity.dockingVehicle = null;
                    blockEntity.dockProgress = 0;
                    blockEntity.dockingStage = 0;
                    if (!level.isClientSide()) {
                        blockEntity.setChanged();
                    }
                }
                break;
            default:
                blockEntity.dockingStage = 0;
                if (!level.isClientSide()) {
                    blockEntity.setChanged();
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
        if (blockEntityVehicleDock.dockingVehicle != null) {
            nbt.putUUID("DockingVehicle",
                    blockEntityVehicleDock.dockingVehicle.getUUID());
        }
        nbt.putInt("DockProgress", blockEntityVehicleDock.dockProgress);
        nbt.putInt("DockStage", blockEntityVehicleDock.dockingStage);
        return nbt;
    }

    public static void load(CompoundTag nbt, BlockEntityVehicleDock blockEntityVehicleDock) {
        if (nbt == null || !nbt.contains("DockingVehicle")) {
            blockEntityVehicleDock.vehicleUuid = null;
        } else {
            blockEntityVehicleDock.vehicleUuid = nbt.getUUID(
                    "DockingVehicle");
        }
        if (nbt == null || !nbt.contains("DockProgress")) {
            blockEntityVehicleDock.dockProgress = 0;
        } else {
            blockEntityVehicleDock.dockProgress = nbt.getInt("DockProgress");
        }
        if (nbt == null || !nbt.contains("DockStage")) {
            blockEntityVehicleDock.dockingStage = 0;
        } else {
            blockEntityVehicleDock.dockingStage = nbt.getInt("DockStage");
        }
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

    public void releaseVehicle() {
        if (this.dockingVehicle != null) {
            this.dockingVehicle.releaseDocking();
        }
        this.dockingVehicle = null;
        this.dockProgress = 0;
        this.dockingStage = 0;
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

    public boolean hasVehicleInRange() {
        return !getNearbyVehicle(this.getLevel(),
                this.worldPosition).isEmpty();
    }
}
