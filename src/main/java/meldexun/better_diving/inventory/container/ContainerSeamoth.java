package meldexun.better_diving.inventory.container;

import meldexun.better_diving.BetterDiving;
import meldexun.better_diving.capability.inventory.item.CapabilityItemHandlerSeamoth;
import meldexun.better_diving.init.BetterDivingItems;
import meldexun.better_diving.item.ItemPowerCell;
import meldexun.better_diving.util.BetterDivingHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.CheckForNull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ContainerSeamoth extends AbstractContainerMenu {

    public static final int FIRST_MODULE_SLOT_OFFSET = 37;

    private final List<Slot> moduleSlots = new ArrayList<>();
    private final List<List<Slot>> inventoryRows = new ArrayList<>();

    /**
     * Server
     */
    public ContainerSeamoth(MenuType<?> type, int id, Inventory playerInv,
                            IItemHandler seamothInv) {
        this(type, playerInv, seamothInv, id);
    }

    /**
     * Client
     */
    public ContainerSeamoth(MenuType<?> type, int id, Inventory playerInv) {
        this(type, playerInv, null, id);
    }

    /**
     * Common constructor
     */
    private ContainerSeamoth(MenuType<?> type, Inventory playerInv,
                             @CheckForNull IItemHandler seamothInv, int id) {
        super(type, id);

        // player inv, 3 x 9
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18,
                        84 + i * 18));
            }
        }

        // player hotbar, 1 x 9
        for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(playerInv, k, 8 + k * 18, 142));
        }

        Container clientSeamothInv = null;

        if (seamothInv == null) {
            clientSeamothInv = new SimpleContainer(
                    CapabilityItemHandlerSeamoth.getSlotCount());
        }

        // seamoth powercell
        int powerCellStorageSlot = 0;
        int powerCellX = 26;
        int powerCellY = 44;
        Slot powerCellSlot = seamothInv == null ?
                // client
                new Slot(clientSeamothInv, powerCellStorageSlot,
                        powerCellX, powerCellY) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return stack.getItem() instanceof ItemPowerCell;
                    }
                } :
                // server
                new SlotItemHandler(seamothInv
                        , powerCellStorageSlot, powerCellX,
                        powerCellY) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return stack.getItem() instanceof ItemPowerCell;
                    }

                    @Override
                    public void setChanged() {
                        ContainerSeamoth.this.onSeamothSlotChanged(
                                this.getSlotIndex());
                    }
                };
        powerCellSlot.setBackground(InventoryMenu.BLOCK_ATLAS,
                new ResourceLocation(BetterDiving.MOD_ID, "item" +
                        "/empty_power_cell"));
        this.addSlot(powerCellSlot);

        // seamoth modules, 4 x 1
        for (int i = 0; i < 4; i++) {
            int moduleX = 62;
            int moduleY = 8 + i * 18;
            int moduleSlotIndex = 1 + i;
            Predicate<ItemStack> moduleMayPlace =
                    itemStack -> BetterDivingHelper.canEquipModule(
                            BetterDiving.Vehicle.Seamoth, itemStack);
            int finalI = i;
            Predicate<Player> moduleMayPickup =
                    player -> !invRowHasItem(finalI);
            Slot slot = seamothInv == null ?
                    // client
                    new Slot(clientSeamothInv, moduleSlotIndex,
                            moduleX, moduleY) {
                        @Override
                        public boolean mayPlace(ItemStack stack) {
                            return moduleMayPlace.test(stack);
                        }

                        @Override
                        public boolean mayPickup(Player playerIn) {
                            return moduleMayPickup.test(playerIn);
                        }

                        @Override
                        public int getMaxStackSize() {
                            return 1;
                        }
                    }
                    :
                    // server
                    new SlotItemHandler(seamothInv, moduleSlotIndex,
                            moduleX, moduleY) {
                        @Override
                        public boolean mayPlace(ItemStack stack) {
                            return moduleMayPlace.test(stack);
                        }

                        @Override
                        public boolean mayPickup(Player playerIn) {
                            return moduleMayPickup.test(playerIn);
                        }

                        @Override
                        public int getMaxStackSize() {
                            return 1;
                        }

                        @Override
                        public void setChanged() {
                            ContainerSeamoth.this.onSeamothSlotChanged(
                                    this.getSlotIndex());
                        }
                    };
            slot.setBackground(InventoryMenu.BLOCK_ATLAS,
                    new ResourceLocation(BetterDiving.MOD_ID,
                            "item" +
                                    "/empty_vehicle_module"));
            this.addSlot(slot);
            this.moduleSlots.add(slot);
        }

        // seamoth inv, 4 x SLOT_PER_STORAGE_MODULE
        int slotPerRow = CapabilityItemHandlerSeamoth.SLOT_PER_STORAGE_MODULE;
        for (int k = 0; k < 4; k++) {
            int finalK = k;
            inventoryRows.add(new ArrayList<>());
            for (int i = 0; i < slotPerRow; i++) {
                int invX = 80 + i * 18;
                int invY = 8 + k * 18;
                int invSlotIndex = 5 + k * slotPerRow + i;
                Predicate<Player> invMayPickup = (player) -> {
                    ItemStack itemStack = moduleSlots.get(finalK).getItem();
                    return itemStack.is(BetterDivingItems.VEHICLE_STORAGE_MODULE.get());
                };
                Predicate<ItemStack> invMayPlace = (itemStack) -> {
                    if (itemStack.is(BetterDivingItems.SEAMOTH.get())) {
                        return false;
                    }
                    ItemStack moduleItemStack =
                            moduleSlots.get(finalK).getItem();
                    return moduleItemStack.is(BetterDivingItems.VEHICLE_STORAGE_MODULE.get());
                };
                Slot invSlot = seamothInv == null ?
                        // client
                        new Slot(clientSeamothInv, invSlotIndex,
                                invX, invY) {

                            @Override
                            public boolean mayPickup(Player playerIn) {
                                return invMayPickup.test(playerIn);
                            }

                            @Override
                            public boolean mayPlace(@NotNull ItemStack stack) {
                                return invMayPlace.test(stack);
                            }
                        }
                        :
                        // server
                        new SlotItemHandler(seamothInv, invSlotIndex,
                                invX, invY) {

                            @Override
                            public boolean mayPickup(Player playerIn) {
                                return invMayPickup.test(playerIn);
                            }

                            @Override
                            public boolean mayPlace(@NotNull ItemStack stack) {
                                return invMayPlace.test(stack);
                            }
                        };
                this.addSlot(invSlot);
                inventoryRows.get(k).add(invSlot);
            }
        }
    }

	@Override
	public boolean stillValid(Player playerIn) {
		return true;
	}

    /**
     * Same as AbstractContainerMenu#moveItemStackTo, except it also checks for
     * Slot#mayPlace when purely increasing count
     *
     * @param p_38904_
     * @param p_38905_
     * @param p_38906_
     * @param p_38907_
     * @return
     */
    @Override
    protected boolean moveItemStackTo(ItemStack p_38904_, int p_38905_, int p_38906_, boolean p_38907_) {
        boolean flag = false;
        int i = p_38905_;
        if (p_38907_) {
            i = p_38906_ - 1;
        }

        Slot slot1;
        ItemStack itemstack;
        if (p_38904_.isStackable()) {
            while (!p_38904_.isEmpty()) {
                if (p_38907_) {
                    if (i < p_38905_) {
                        break;
                    }
                } else if (i >= p_38906_) {
                    break;
                }

                slot1 = (Slot) this.slots.get(i);
                itemstack = slot1.getItem();
                // !!! BIG OBVIOUS RIGHT HERE !!!
                //              |
                //              |
                //              V
                if (slot1.mayPlace(
                        p_38904_) && !itemstack.isEmpty() && ItemStack.isSameItemSameTags(
                        p_38904_, itemstack)) {
                    int j = itemstack.getCount() + p_38904_.getCount();
                    int maxSize = Math.min(slot1.getMaxStackSize(),
                            p_38904_.getMaxStackSize());
                    if (j <= maxSize) {
                        p_38904_.setCount(0);
                        itemstack.setCount(j);
                        slot1.setChanged();
                        flag = true;
                    } else if (itemstack.getCount() < maxSize) {
                        p_38904_.shrink(maxSize - itemstack.getCount());
                        itemstack.setCount(maxSize);
                        slot1.setChanged();
                        flag = true;
                    }
                }

                if (p_38907_) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        if (!p_38904_.isEmpty()) {
            if (p_38907_) {
                i = p_38906_ - 1;
            } else {
                i = p_38905_;
            }

            while (true) {
                if (p_38907_) {
                    if (i < p_38905_) {
                        break;
                    }
                } else if (i >= p_38906_) {
                    break;
                }

                slot1 = (Slot) this.slots.get(i);
                itemstack = slot1.getItem();
                if (itemstack.isEmpty() && slot1.mayPlace(p_38904_)) {
                    if (p_38904_.getCount() > slot1.getMaxStackSize()) {
                        slot1.setByPlayer(
                                p_38904_.split(slot1.getMaxStackSize()));
                    } else {
                        slot1.setByPlayer(p_38904_.split(p_38904_.getCount()));
                    }

                    slot1.setChanged();
                    flag = true;
                    break;
                }

                if (p_38907_) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        return flag;
    }

	@Override
	public ItemStack quickMoveStack(Player playerIn, int index) {
		Slot slot = this.slots.get(index);

		if (slot == null) {
			return ItemStack.EMPTY;
		}

        ItemStack stack = slot.getItem();

        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        if (index > 35) {
            if (!this.moveItemStackTo(stack, 0, 35, false)) {
                return ItemStack.EMPTY;
            }
        } else if (!this.moveItemStackTo(stack, 36, this.slots.size(), false)) {
            return ItemStack.EMPTY;
        }

        slot.setChanged();
        return stack;
    }

    protected void onSeamothSlotChanged(int slot) {

    }

    public boolean invRowHasItem(int rowIndex) {
        for (Slot slot: inventoryRows.get(rowIndex)) {
            if (!slot.getItem().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}