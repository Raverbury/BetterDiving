package meldexun.better_diving.entity;

import meldexun.better_diving.BetterDiving;
import meldexun.better_diving.client.ClientBetterDiving;
import meldexun.better_diving.client.audio.SeamothEngineLoopSound;
import meldexun.better_diving.client.audio.SeamothStartSound;
import meldexun.better_diving.config.BetterDivingConfig;
import meldexun.better_diving.init.BetterDivingItems;
import meldexun.better_diving.init.BetterDivingSounds;
import meldexun.better_diving.inventory.container.ContainerSeamothEntity;
import meldexun.better_diving.network.packet.client.CPacketSyncSeamothInput;
import meldexun.better_diving.util.BetterDivingHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidType;

import java.util.List;

public class EntitySeamoth extends EntityPowerCellPoweredVehicle {

    public boolean inputForward = false;
    public boolean inputRight = false;
    public boolean inputBack = false;
    public boolean inputLeft = false;
    public boolean inputUp = false;
    public boolean inputDown = false;
    public boolean insideWater = false;
    @OnlyIn(Dist.CLIENT)
    public SeamothStartSound startSound;
    @OnlyIn(Dist.CLIENT)
    public SeamothEngineLoopSound engineLoopSound;
    private int damage = 0;
    private boolean controlled = false;
    private boolean prevControlled = false;
    private boolean prevSteered = false;
    private int prevEnergy;
    private long prevWelcomeTimestamp = 0;
    private boolean prevWelcomeHasEnergy = true;

    public EntitySeamoth(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Override
    protected float getEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return sizeIn.height * 0.5F;
    }

    @OnlyIn(Dist.CLIENT)
    public void updateEngineSound() {
        Minecraft mc = Minecraft.getInstance();

        if (mc.options.getSoundSourceVolume(SoundSource.MASTER) > 0.0F) {
            SoundManager soundHandler = mc.getSoundManager();

            if (!soundHandler.isActive(this.startSound)) {
                if (!this.prevSteered && this.isPlayerSteering() && this.hasEnergy() && !soundHandler.isActive(
                        this.engineLoopSound)) {
                    this.startSound = new SeamothStartSound(this);
                    soundHandler.play(this.startSound);
                } else if (this.startSound != null) {
                    this.startSound = null;
                }
            }

            if (!soundHandler.isActive(this.engineLoopSound)) {
                if (this.isPlayerSteering() && this.hasEnergy()) {
                    int tick = this.engineLoopSound != null && this.engineLoopSound.getTick() > 0 ? this.engineLoopSound.getTick() : 0;
                    this.engineLoopSound = new SeamothEngineLoopSound(this);
                    this.engineLoopSound.setTick(tick);
                    soundHandler.play(this.engineLoopSound);
                } else if (this.engineLoopSound != null) {
                    this.engineLoopSound = null;
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void updateControls() {
        Minecraft mc = Minecraft.getInstance();

        this.prevControlled = this.controlled;
        this.controlled = this.getControllingPassenger() == mc.player;
        this.prevSteered = this.isPlayerSteering();

        if (this.controlled) {
            Options settings = mc.options;

            this.inputForward = settings.keyUp.isDown();
            this.inputRight = settings.keyRight.isDown();
            this.inputBack = settings.keyDown.isDown();
            this.inputLeft = settings.keyLeft.isDown();
            this.inputUp = settings.keyJump.isDown();
            this.inputDown = ClientBetterDiving.KEY_BIND_DESCEND.isDown();

            BetterDiving.NETWORK.sendToServer(
                    new CPacketSyncSeamothInput(this));
        } else if (this.prevControlled) {
            this.inputForward = false;
            this.inputRight = false;
            this.inputBack = false;
            this.inputLeft = false;
            this.inputUp = false;
            this.inputDown = false;

            BetterDiving.NETWORK.sendToServer(
                    new CPacketSyncSeamothInput(this));
        }
    }

    @Override
    public void tick() {
        if (this.level()
                .isClientSide() && this.getControllingPassenger() instanceof Player && ((Player) this.getControllingPassenger()).isLocalPlayer()) {
            this.syncPacketPositionCodec(this.position().x(),
                    this.position().y(), this.position().z());
        }

        super.tick();

        this.setOnGround(false);
        this.insideWater = this.isEyeInFluid(FluidTags.WATER);

        if (this.damage > 0) {
            this.damage--;
        }

        if (this.level().isClientSide()) {
            this.updateControls();
            this.updateEngineSound();
        }

        this.updateMotion();

        this.move(MoverType.SELF, this.getDeltaMovement());

        if (!this.level().isClientSide()) {
            if (this.getEnergy() != this.prevEnergy) {
                this.syncEnergy();
            }
            this.prevEnergy = this.getEnergy();
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!this.level()
                .isClientSide() && !this.isRemoved() && source.getEntity() instanceof Player) {
            Player player = (Player) source.getEntity();

            if (player.isCreative()) {
                this.kill();
                return true;
            } else if (!this.isVehicle()) {
                this.damage += 20;
                if (this.damage > 30) {
                    ItemStack seamoth = this.toItemStack();
                    if (!player.addItem(seamoth)) {
                        this.spawnAtLocation(seamoth, 0.0F);
                    }

                    this.kill();
                }
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canBeCollidedWith() {
        return !this.isRemoved();
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (this.isVehicle()) {
            return InteractionResult.FAIL;
        }
        if (!this.level().isClientSide()) {
            if (player.isShiftKeyDown()) {
                player.openMenu(new SimpleMenuProvider(
                        (id, playerInv, player2) -> new ContainerSeamothEntity(
                                id,
                                playerInv,
                                this),
                        Component.translatable("Seamoth")));
            } else {
                player.startRiding(this);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
        countEngineEfficiencyModules(1);
        passenger.setYRot(this.getYRot());
        passenger.setXRot(this.getXRot());
        passenger.setPose(Pose.STANDING);
        if (passenger instanceof Player player) {
            player.updatePlayerPose();
        }
        passenger.refreshDimensions();
        if (!this.level().isClientSide()) {
            this.syncPowerCell();
        } else if (passenger instanceof Player) {
            this.level().playSound((Player) passenger,
                    this.blockPosition(),
                    BetterDivingSounds.SEAMOTH_ENTER.get(),
                    this.getSoundSource(), 1.0F, 1.0F);
            // play seamoth welcome/no power sound
            long currentTimestamp = this.level().getGameTime();
            boolean hasEnergy = this.hasEnergy();
            // 120 seconds
            long soundInterval = 2400;
            // shorter interval if hasEnergy is different
            if (hasEnergy != prevWelcomeHasEnergy) {
                // 1 seconds
                soundInterval = 20;
            }
            if (currentTimestamp - prevWelcomeTimestamp > soundInterval) {
                this.level().playSound((Player) passenger,
                        this.blockPosition(),
                        hasEnergy?
                                BetterDivingSounds.SEAMOTH_WELCOME.get() :
                                BetterDivingSounds.SEAMOTH_NO_POWER.get(),
                        this.getSoundSource(), 1.0F, 1.0F);
                prevWelcomeTimestamp = currentTimestamp;
                prevWelcomeHasEnergy = hasEnergy;
            }
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        return 0.03D;
    }

    @Override
    public MovementEmission getMovementEmission() {
        return MovementEmission.NONE;
    }

    @Override
    public boolean shouldRiderSit() {
        return true;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean canBeRiddenUnderFluidType(FluidType fluidType, Entity entity) {
        return fluidType.canSwim(entity);
    }

    @Override
    public LivingEntity getControllingPassenger() {
        List<Entity> list = this.getPassengers();
        return list.isEmpty() ? null : (LivingEntity) list.get(0);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onPassengerTurned(Entity entityToUpdate) {
        if (this.isDocked()) {
            entityToUpdate.setYRot(this.yRotO);
            entityToUpdate.setXRot(this.xRotO);
            return;
        }
        this.yRotO = entityToUpdate.yRotO;
        this.setYRot(entityToUpdate.getYRot());
        this.xRotO = entityToUpdate.xRotO;
        this.setXRot(entityToUpdate.getXRot());
    }

    public void updateMotion() {
        if (this.insideWater) {
            this.setDeltaMovement(this.getDeltaMovement().scale(0.95D));
        } else {
            this.setDeltaMovement(this.getDeltaMovement().scale(0.99D));
        }

        if (Math.abs(this.getDeltaMovement().x) < 0.001D) {
            this.setDeltaMovement(new Vec3(0.0D, this.getDeltaMovement().y,
                    this.getDeltaMovement().z));
        }
        if (Math.abs(this.getDeltaMovement().y) < 0.001D) {
            this.setDeltaMovement(new Vec3(this.getDeltaMovement().x, 0.0D,
                    this.getDeltaMovement().z));
        }
        if (Math.abs(this.getDeltaMovement().z) < 0.001D) {
            this.setDeltaMovement(new Vec3(this.getDeltaMovement().x,
                    this.getDeltaMovement().y, 0.0D));
        }

        if (this.isDocked()) {
            this.setDeltaMovement(Vec3.ZERO);
            return;
        }

        if (!this.insideWater) {
            this.setDeltaMovement(
                    this.getDeltaMovement().subtract(0.0D, 0.015D, 0.0D));
        }

        if (this.getControllingPassenger() instanceof Player && this.isPlayerSteering() && this.hasEnergy()) {
            if (!this.level().isClientSide()) {
                this.consumeEnergy(
                        BetterDivingConfig.SERVER_CONFIG.seamoth.seamothEnergyUsage.get());
            }

            if (this.insideWater) {
                double speed = BetterDivingConfig.SERVER_CONFIG.seamoth.seamothSpeed.get();

                double forward = 0.0D;
                double up = 0.0D;
                double strafe = 0.0D;

                if (this.inputForward) {
                    forward += 1.0D;
                }
                if (this.inputBack) {
                    forward -= 1.0D;
                }
                if (this.inputUp) {
                    up += 1.0D;
                }
                if (this.inputDown) {
                    up -= 1.0D;
                }
                if (this.inputRight) {
                    strafe -= 1.0D;
                }
                if (this.inputLeft) {
                    strafe += 1.0D;
                }

                if (forward < 0.0D) {
                    if (up == 0.0D) {
                        if (strafe == 0.0D) {
                            speed *= 0.5D;
                        } else {
                            speed *= 0.7071D;
                        }
                    } else {
                        if (strafe == 0.0D) {
                            speed *= 0.7071D;
                        } else {
                            speed *= 0.8409D;
                        }
                    }
                }

                Vec3 vec = BetterDivingHelper.getSeamothMoveVec(forward, up,
                        strafe, speed, this.getYRot(), this.getXRot());
                this.setDeltaMovement(this.getDeltaMovement().add(vec));
            }
        }
    }

    public boolean isPlayerSteering() {
        return this.inputForward != this.inputBack || this.inputLeft != this.inputRight || this.inputUp != this.inputDown;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected boolean canRide(Entity entity) {
        return true;
    }

    @Override
    public void onAboveBubbleCol(boolean pullingDown) {
        // do nothing
    }

    @Override
    public void onInsideBubbleColumn(boolean pullingDown) {
        // do nothing
    }

    public ItemStack toItemStack() {
        ItemStack stack = new ItemStack(BetterDivingItems.SEAMOTH.get());

        stack.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(c -> {
            this.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(c1 -> {
                for (int i = 0; i < c1.getSlots(); i++) {
                    c.insertItem(i, c1.getStackInSlot(i).copy(), false);
                }
            });
        });
        return stack;
    }
}
