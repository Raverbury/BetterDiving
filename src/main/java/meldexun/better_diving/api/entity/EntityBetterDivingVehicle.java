package meldexun.better_diving.api.entity;

import meldexun.better_diving.BetterDiving;
import meldexun.better_diving.network.packet.server.SPacketUndockVehicle;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;

public abstract class EntityBetterDivingVehicle extends Entity {

    protected boolean isDocked = false;

    public EntityBetterDivingVehicle(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void addPassenger(Entity p_20349_) {
        super.addPassenger(p_20349_);
        if (!this.level().isClientSide()) {
            releaseDocking();
            BetterDiving.NETWORK.send(
                    PacketDistributor.TRACKING_ENTITY.with(() -> this),
                    new SPacketUndockVehicle(this));
        }
    }

    public void startDocking() {
        setNoGravity(true);
        isDocked = true;
    }

    public void releaseDocking() {
        setNoGravity(isNoGravity());
        isDocked = false;
    }

    public abstract int receiveEnergy(int amount);

    public abstract int getEnergyCapacity();

    public abstract int getEnergy();

    public boolean isDocked() {
        return this.isDocked;
    }
}
