package meldexun.better_diving.capability.inventory.item;

import net.minecraftforge.items.ItemStackHandler;

public class CapabilityItemHandlerSeamoth extends ItemStackHandler {

    public static final int SLOT_PER_STORAGE_MODULE = 5;
    public static final int FIRST_MODULE_INDEX = 1;

    public CapabilityItemHandlerSeamoth() {
        super(getSlotCount());
    }

    public static int getSlotCount() {
        return 5 + 4 * SLOT_PER_STORAGE_MODULE;
    }

    @Override
    public void setSize(int size) {
    }

    @Override
    public int getSlotLimit(int slot) {
        if (slot > 0 && slot < 5) {
            return 1;
        }
        return super.getSlotLimit(slot);
    }

    @Override
    public void onContentsChanged(int slot) {
        this.serializeNBT();
    }
}
