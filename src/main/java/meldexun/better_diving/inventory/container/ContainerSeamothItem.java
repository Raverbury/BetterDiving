package meldexun.better_diving.inventory.container;

import meldexun.better_diving.capability.inventory.item.CapabilityItemHandlerItem;
import meldexun.better_diving.init.BetterDivingContainers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;

public class ContainerSeamothItem extends ContainerSeamoth {

	private final IItemHandler seamothInv;
	private final DataSlot hand = DataSlot.standalone();

	/** Server */
	public ContainerSeamothItem(int id, Inventory playerInv, ItemStack stack,
								InteractionHand hand) {
		super(BetterDivingContainers.SEAMOTH_ITEM.get(), id, playerInv,
				stack.getCapability(ForgeCapabilities.ITEM_HANDLER).orElseThrow(NullPointerException::new));
		this.seamothInv =
				stack.getCapability(ForgeCapabilities.ITEM_HANDLER).orElseThrow(NullPointerException::new);
		this.addDataSlot(this.hand);
		this.hand.set(hand.ordinal());
		int i = 27 + playerInv.selected;
		Slot slot = this.slots.get(i);
		this.slots.set(i, new Slot(playerInv, playerInv.selected, slot.x, slot.y) {
			@Override
			public boolean mayPickup(Player playerIn) {
				return ContainerSeamothItem.this.hand.get() != 0;
			}
		});
		this.slots.get(i).index = i;
	}

	/** Client */
	public ContainerSeamothItem(int id, Inventory playerInv) {
		super(BetterDivingContainers.SEAMOTH_ITEM.get(), id, playerInv);
		this.seamothInv = null;
		this.addDataSlot(this.hand);
		int i = 27 + playerInv.selected;
		Slot slot = this.slots.get(i);
		this.slots.set(i, new Slot(playerInv, playerInv.selected, slot.x, slot.y) {
			@Override
			public boolean mayPickup(Player playerIn) {
				return ContainerSeamothItem.this.hand.get() != 0;
			}
		});
		this.slots.get(i).index = i;
	}

	public int getHand() {
		return this.hand.get();
	}

	@Override
	protected void onSeamothSlotChanged(int slot) {
		if (this.seamothInv instanceof CapabilityItemHandlerItem) {
			((CapabilityItemHandlerItem) this.seamothInv).onContentsChanged(slot);
		}
	}

}
