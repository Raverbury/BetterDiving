package meldexun.better_diving.blockentity;

import meldexun.better_diving.BetterDiving;
import meldexun.better_diving.api.entity.EntityBetterDivingVehicle;
import meldexun.better_diving.config.BetterDivingConfig;
import meldexun.better_diving.init.BetterDivingBlockEntities;
import meldexun.better_diving.network.packet.server.SPacketSyncDockingVehicle;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
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
import java.util.UUID;

public class BlockEntityVehicleDock extends BlockEntity {

    private int dockingStage = 0;
    private int dockProgress = 0;
    private UUID vehicleUuid = null;
    private boolean hasLoaded = false;
    private EntityBetterDivingVehicle dockingVehicle = null;

    private LazyOptional<IEnergyStorage> energyCap = LazyOptional.empty();

    public BlockEntityVehicleDock(BlockPos p_155229_, BlockState p_155230_) {
        super(BetterDivingBlockEntities.DOCKING_BE.get(), p_155229_, p_155230_);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, BlockEntityVehicleDock blockEntity) {
        // first tick make sure entity is grabbed from saved uuid if any
        // prob could move this back in load(), legacy from ticking on both
        // sides
        if (!blockEntity.hasLoaded) {
            blockEntity.energyCap =
                    blockEntity.getCapability(ForgeCapabilities.ENERGY);
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

        // recharge solar
        if (level.getGameTime() % 40 == 0) {
            if (level.canSeeSky(pos.above())) {
                level.updateSkyBrightness();
                if (level.isDay()) {
                    blockEntity.energyCap.ifPresent((c) -> c.receiveEnergy(BetterDivingConfig.SERVER_CONFIG.dockingBlock.dockBlockSolarGenerationRate.get(),
                            false));
                }
            }
        }

        // docking process
        switch (blockEntity.dockingStage) {
            // wait until there's a valid vehicle in range
            case 0:
                EntityBetterDivingVehicle vehicle = getFirstNearbyVehicle(
                        level, pos);
                if (vehicle != null) {
                    blockEntity.dockingVehicle = vehicle;
                    blockEntity.dockingStage = 1;
                    blockEntity.setChanged();
                }
                break;
            // disable control, slowly lift/move the vehicle up
            case 1:
                if (blockEntity.verifyDockingVehicle()) {
                    double dockX = pos.getX() + 0.5;
                    double dockY = pos.getY() - 3.5 - blockEntity.dockingVehicle.getBoundingBox()
                            .getYsize();
                    double dockZ = pos.getZ() + 0.5;
                    blockEntity.dockingVehicle.startDocking();
                    blockEntity.dockingVehicle.setDeltaMovement(Vec3.ZERO);
                    blockEntity.dockProgress += 1;

                    float newYaw = Mth.lerpInt(blockEntity.dockProgress / 30f,
                            (int) blockEntity.dockingVehicle.getYRot(),
                            getYawTarget(blockEntity.dockingVehicle));
                    blockEntity.dockingVehicle.setYRot(
                            newYaw);

                    float newPitch =
                            Mth.lerpInt(blockEntity.dockProgress / 25f,
                                    (int) blockEntity.dockingVehicle.getXRot(),
                                    0);
                    blockEntity.dockingVehicle.setXRot(newPitch);

                    Vec3 vehiclePos = blockEntity.dockingVehicle.position();
                    double newX = Mth.lerp(blockEntity.dockProgress / 45.0,
                            vehiclePos.x, dockX);
                    double yLerpScale = 0.05;
                    if (blockEntity.dockProgress > 25) {
                        yLerpScale = 0.4;
                    }
                    double newY =
                            Mth.lerp(
                                    yLerpScale * blockEntity.dockProgress / 45.0f,
                                    vehiclePos.y, dockY);
                    double newZ = Mth.lerp(blockEntity.dockProgress / 45.0,
                            vehiclePos.z, dockZ);
                    blockEntity.dockingVehicle.moveTo(newX, newY, newZ);
                    blockEntity.dockingVehicle.hasImpulse = true;

                    if (blockEntity.dockProgress >= 50) {
                        newX = dockX;
                        newY = dockY;
                        newZ = dockZ;
                        blockEntity.dockingVehicle.moveTo(newX, newY, newZ);
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
                    syncVehicle(blockEntity.dockingVehicle, (float) newX,
                            (float) newY, (float) newZ,
                            newYaw, newPitch);
                } else {
                    blockEntity.releaseVehicle();
                }
                blockEntity.setChanged();
                break;
            // wait until a player enters vehicle which sets the shouldUndock
            // flag
            // release vehicle after some time when shouldUndock, then forget
            // and reset
            case 2:
                if (blockEntity.verifyDockingVehicle()) {
                    double dockX = pos.getX() + 0.5;
                    double dockY =
                            pos.getY() - 3.5 - blockEntity.dockingVehicle.getBoundingBox()
                                    .getYsize();
                    double dockZ = pos.getZ() + 0.5;
                    blockEntity.dockingVehicle.moveTo(dockX, dockY, dockZ);
                    blockEntity.dockingVehicle.setDeltaMovement(Vec3.ZERO);
                    blockEntity.dockingVehicle.hasImpulse = true;
                    // syncVehicle(blockEntity.dockingVehicle, (float) dockX,
                    //         (float) dockZ, (float) dockY,
                    //         blockEntity.dockingVehicle.getYRot(),
                    //         blockEntity.dockingVehicle.getXRot());
                    int missingEnergy =
                            blockEntity.dockingVehicle.getEnergyCapacity() - blockEntity.dockingVehicle.getEnergy();
                    if (missingEnergy > 0) {
                        blockEntity.energyCap.ifPresent((c) -> {
                            int transferAmount = c.extractEnergy(
                                    BetterDivingConfig.SERVER_CONFIG.dockingBlock.dockBlockEnergyTransferRate.get(),
                                    false);
                            blockEntity.dockingVehicle.receiveEnergy(
                                    transferAmount);
                        });
                    }
                    if (blockEntity.dockingVehicle.shouldUndock) {
                        blockEntity.dockProgress -= 5;
                        if (blockEntity.dockProgress <= 0) {
                            blockEntity.dockingVehicle.releaseDocking();
                            blockEntity.dockingVehicle = null;
                            blockEntity.dockingStage = 3;
                        }
                        blockEntity.setChanged();
                    }
                } else {
                    blockEntity.releaseVehicle();
                    blockEntity.setChanged();
                }
                break;
            // wait until no vehicle is in range before resetting to stage 0
            // this is so the dock doesnt pick up the recently undocked vehicle
            case 3:
                List<EntityBetterDivingVehicle> vehicles = getNearbyVehicle(
                        level, pos);
                if (vehicles.isEmpty()) {
                    blockEntity.dockingVehicle = null;
                    blockEntity.dockProgress = 0;
                    blockEntity.dockingStage = 0;
                    blockEntity.setChanged();
                }
                break;
            default:
                blockEntity.dockingStage = 0;
                blockEntity.setChanged();
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

    private static void syncVehicle(EntityBetterDivingVehicle vehicle,
                                    float x, float y, float z,
                                    float yaw, float pitch) {
        if (vehicle.getControllingPassenger() instanceof ServerPlayer) {
            BetterDiving.NETWORK.send(
                    PacketDistributor.PLAYER.with(
                            () -> (ServerPlayer) vehicle.getControllingPassenger()),
                    new SPacketSyncDockingVehicle(vehicle, x,
                            y, z,
                            yaw, pitch));
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

    /**
     * 0 if no vehicle, 1 to 15 with vehicle depending on energy fullness
     * @return
     */
    public int getRedstoneSignal() {
        if (this.dockingVehicle == null) {
            return 0;
        }
        int energyCap = this.dockingVehicle.getEnergyCapacity();
        if (energyCap == 0) {
            return 15;
        }
        return 1 + (int)(14f * this.dockingVehicle.getEnergy() / this.dockingVehicle.getEnergyCapacity());
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        energyCap.invalidate();
    }
}
