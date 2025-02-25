package meldexun.better_diving.inventory.container;

import meldexun.better_diving.BetterDiving;
import meldexun.better_diving.item.ItemPowerCell;
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

public class ContainerSeamoth extends AbstractContainerMenu {

	/** Server */
	public ContainerSeamoth(MenuType<?> type, int id, Inventory playerInv,
							IItemHandler seamothInv) {
		super(type, id);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18,
						84 + i * 18));
			}
		}

		for (int k = 0; k < 9; k++) {
			this.addSlot(new Slot(playerInv, k, 8 + k * 18, 142));
		}

		this.addSlot(new SlotItemHandler(seamothInv, 0, 62, 50) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return stack.getItem() instanceof ItemPowerCell;
			}

			@Override
			public void setChanged() {
				ContainerSeamoth.this.onSeamothSlotChanged(this.getSlotIndex());
			}
		}.setBackground(InventoryMenu.BLOCK_ATLAS,
				new ResourceLocation(BetterDiving.MOD_ID, "item" +
						"/empty_power_cell")));
	}

	/** Client */
	public ContainerSeamoth(MenuType<?> type, int id, Inventory playerInv) {
		super(type, id);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int k = 0; k < 9; k++) {
			this.addSlot(new Slot(playerInv, k, 8 + k * 18, 142));
		}

		Container seamothInv = new SimpleContainer(1);
		this.addSlot(new Slot(seamothInv, 0, 62, 50) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return stack.getItem() instanceof ItemPowerCell;
			}
		}.setBackground(InventoryMenu.BLOCK_ATLAS,
				new ResourceLocation(BetterDiving.MOD_ID, "item/empty_power_cell")));
	}

	@Override
	public boolean stillValid(Player playerIn) {
		return true;
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

}