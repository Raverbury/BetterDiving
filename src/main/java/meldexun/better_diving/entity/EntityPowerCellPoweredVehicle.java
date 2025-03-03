package meldexun.better_diving.entity;

import meldexun.better_diving.BetterDiving;
import meldexun.better_diving.api.entity.EntityBetterDivingVehicle;
import meldexun.better_diving.item.ItemEnergyStorage;
import meldexun.better_diving.item.ItemPowerCell;
import meldexun.better_diving.network.packet.server.SPacketSyncPowerCellVehicleEnergy;
import meldexun.better_diving.network.packet.server.SPacketSyncVehiclePowerCell;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;

public class EntityPowerCellPoweredVehicle extends EntityBetterDivingVehicle implements IEntityAdditionalSpawnData {
    public EntityPowerCellPoweredVehicle(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Override
    public int receiveEnergy(int amount) {
        if (amount > 0) {
            ItemStack powerCell = this.getPowerCell();
            if (powerCell.getItem() instanceof ItemPowerCell) {
                return ItemEnergyStorage.receiveEnergy(powerCell, amount);
            }
        }
        return 0;
    }

    @Override
    public int getEnergyCapacity() {
        ItemStack powerCell = this.getPowerCell();
        if (powerCell.getItem() instanceof ItemPowerCell) {
            return ItemEnergyStorage.getEnergyCapacity(powerCell);
        }
        return 0;
    }

    @Override
    public int getEnergy() {
        ItemStack powerCell = this.getPowerCell();
        if (powerCell.getItem() instanceof ItemPowerCell) {
            return ItemEnergyStorage.getEnergy(powerCell);
        }
        return 0;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        this.entityData.set(IS_DOCKED, compoundTag.contains(
                "IsDocked") && compoundTag.getBoolean("IsDocked"));
        this.shouldUndock = compoundTag.contains(
                "ShouldUndock") && compoundTag.getBoolean("ShouldUndock");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putBoolean("IsDocked", this.entityData.get(IS_DOCKED));
        compoundTag.putBoolean("ShouldUndock", this.shouldUndock);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeItemStack(this.getPowerCell(), false);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf friendlyByteBuf) {
        this.setPowerCell(friendlyByteBuf.readItem());
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public ItemStack getPowerCell() {
        LazyOptional<IItemHandler> optionalItemHandler =
                this.getCapability(ForgeCapabilities.ITEM_HANDLER);
        if (!optionalItemHandler.isPresent()) {
            return ItemStack.EMPTY;
        }
        IItemHandler itemHandler = optionalItemHandler.orElseThrow(
                NullPointerException::new);
        return itemHandler.getStackInSlot(0);
    }

    public void setPowerCell(ItemStack stack) {
        this.getCapability(
                ForgeCapabilities.ITEM_HANDLER).ifPresent(c -> {
            ((ItemStackHandler) c).setStackInSlot(0, stack);
        });
    }

    public boolean hasEnergy() {
        ItemStack powerCell = this.getPowerCell();
        if (powerCell.getItem() instanceof ItemPowerCell) {
            return ItemEnergyStorage.hasEnergy(powerCell);
        }
        return false;
    }

    public boolean setEnergy(int energy) {
        ItemStack powerCell = this.getPowerCell();
        if (powerCell.getItem() instanceof ItemPowerCell) {
            return ItemEnergyStorage.setEnergy(powerCell, energy);
        }
        return false;
    }

    public int extractEnergy(int amount) {
        if (amount > 0) {
            ItemStack powerCell = this.getPowerCell();
            if (powerCell.getItem() instanceof ItemPowerCell) {
                return ItemEnergyStorage.extractEnergy(powerCell, amount);
            }
        }
        return 0;
    }

    public void syncPowerCell() {
        if (!this.level()
                .isClientSide() && this.getControllingPassenger() instanceof Player) {
            Player player = (Player) this.getControllingPassenger();
            BetterDiving.NETWORK.send(
                    PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
                    new SPacketSyncVehiclePowerCell(this));
        }
    }

    public void syncEnergy() {
        if (!this.level()
                .isClientSide() && this.getControllingPassenger() instanceof Player) {
            Player player = (Player) this.getControllingPassenger();
            BetterDiving.NETWORK.send(
                    PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
                    new SPacketSyncPowerCellVehicleEnergy(this));
        }
    }
}
