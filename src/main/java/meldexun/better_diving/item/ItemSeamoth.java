package meldexun.better_diving.item;

import meldexun.better_diving.capability.inventory.item.CapabilityItemHandlerItem;
import meldexun.better_diving.capability.inventory.item.CapabilityItemHandlerItemProvider;
import meldexun.better_diving.entity.EntitySeamoth;
import meldexun.better_diving.init.BetterDivingEntities;
import meldexun.better_diving.init.BetterDivingItems;
import meldexun.better_diving.inventory.container.ContainerSeamothItem;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

public class ItemSeamoth extends Item {

    public ItemSeamoth() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack,
                                                CompoundTag nbt) {
        return new CapabilityItemHandlerItemProvider(
                () -> new CapabilityItemHandlerItem(stack, 1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn,
                                                  InteractionHand handIn) {
        if (!worldIn.isClientSide()) {
            if (playerIn.isShiftKeyDown()) {
                playerIn.openMenu(new SimpleMenuProvider(
                        (id, playerInv, player) -> new ContainerSeamothItem(id, playerInv,
                                playerIn.getItemInHand(handIn), handIn), Component.translatable("Seamoth")));
            } else {
                EntitySeamoth seamoth = new EntitySeamoth(
                        BetterDivingEntities.SEAMOTH.get(), worldIn);

                playerIn.getItemInHand(handIn).getCapability(
                                ForgeCapabilities.ITEM_HANDLER)
                        .ifPresent(c -> {
                            seamoth.getCapability(
                                            ForgeCapabilities.ITEM_HANDLER)
                                    .ifPresent(c1 -> {
                                        c1.insertItem(0,
                                                c.getStackInSlot(0).copy(),
                                                false);
                                    });
                        });

                Vec3 start = playerIn.getEyePosition(1.0F);
                Vec3 look = playerIn.getLookAngle();
                Vec3 end = start.add(look.scale(5.0D));
                BlockHitResult result = worldIn.clip(
                        new ClipContext(start, end, ClipContext.Block.COLLIDER,
                                ClipContext.Fluid.NONE, null));
                Vec3 vec =
                        (result != null ? result.getLocation() : end).subtract(
                                look);

                seamoth.setPos(vec.x, vec.y, vec.z);
                seamoth.setYRot(playerIn.getYRot());
                worldIn.addFreshEntity(seamoth);

                if (worldIn.getFluidState(seamoth.blockPosition())
                        .is(FluidTags.WATER)) {
                    seamoth.playSound(SoundEvents.AMBIENT_UNDERWATER_ENTER,
                            1.0F, 0.9F + worldIn.random.nextFloat() * 0.2F);
                }

                if (!playerIn.isCreative()) {
                    playerIn.getItemInHand(handIn).shrink(1);
                }
            }
        }
        return InteractionResultHolder.success(playerIn.getItemInHand(handIn));
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn,
                                List<Component> tooltip, TooltipFlag flagIn) {
        ItemStack powerCell = ItemStack.EMPTY;
        LazyOptional<IItemHandler> cap =
                stack.getCapability(ForgeCapabilities.ITEM_HANDLER);
        if (cap.isPresent()) {
            powerCell = cap.orElseThrow(NullPointerException::new)
                    .getStackInSlot(0);
        }
        if (!powerCell.isEmpty() && powerCell.getItem() instanceof ItemPowerCell) {
            int energy = Mth.ceil(
                    ItemEnergyStorage.getEnergyPercent(powerCell) * 100.0D);
            if (flagIn.isAdvanced()) {
                tooltip.add(Component.literal(
                        ChatFormatting.GRAY + String.format(
                                "Energy %d%% (%d/%d)", energy,
                                ItemEnergyStorage.getEnergy(powerCell),
                                ItemEnergyStorage.getEnergyCapacity(
                                        powerCell))));
            } else {
                tooltip.add(Component.literal(
                        ChatFormatting.GRAY + String.format("Energy %d%%",
                                energy)));
            }
        } else {
            tooltip.add(Component.literal(
                    ChatFormatting.GRAY + "No power cell"));
        }
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level worldIn,
                            Player playerIn) {
        super.onCraftedBy(stack, worldIn, playerIn);
        stack.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(c -> {
            ((ItemStackHandler) c).setStackInSlot(0,
                    new ItemStack(BetterDivingItems.POWER_CELL.get()));
        });
    }

}
