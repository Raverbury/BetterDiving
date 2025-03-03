package meldexun.better_diving.api.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public abstract class EntityBetterDivingVehicle extends Entity {

    protected static final EntityDataAccessor<Boolean> IS_DOCKED =
            SynchedEntityData.defineId(EntityBetterDivingVehicle.class,
                    EntityDataSerializers.BOOLEAN);
    public boolean shouldUndock = false;

    public EntityBetterDivingVehicle(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(IS_DOCKED, false);
    }

    public void startDocking() {
        setNoGravity(true);
        entityData.set(IS_DOCKED, true);
    }

    public void releaseDocking() {
        setNoGravity(isNoGravity());
        entityData.set(IS_DOCKED, false);
    }

    public abstract int receiveEnergy(int amount);

    public abstract int getEnergyCapacity();

    public abstract int getEnergy();

    public boolean isDocked() {
        return entityData.get(IS_DOCKED);
    }
}
