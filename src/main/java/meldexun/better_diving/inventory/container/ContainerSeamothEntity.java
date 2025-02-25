package meldexun.better_diving.inventory.container;

import meldexun.better_diving.entity.EntitySeamoth;
import meldexun.better_diving.init.BetterDivingContainers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public class ContainerSeamothEntity extends ContainerSeamoth {

	private final EntitySeamoth seamoth;
	private final DataSlot entity = DataSlot.standalone();

	/** Server */
	public ContainerSeamothEntity(int id, Inventory playerInv,
								  EntitySeamoth seamoth) {
		super(BetterDivingContainers.SEAMOTH_ENTITY.get(), id, playerInv,
                seamoth.getCapability(ForgeCapabilities.ITEM_HANDLER).orElseThrow(NullPointerException::new));
		this.seamoth = seamoth;
		this.addDataSlot(this.entity);
		this.entity.set(seamoth.getId());
	}

	/** Client */
	public ContainerSeamothEntity(int id, Inventory playerInv) {
		super(BetterDivingContainers.SEAMOTH_ENTITY.get(), id, playerInv);
		this.seamoth = null;
		this.addDataSlot(this.entity);
	}

	public int getEntityId() {
		return this.entity.get();
	}

	@Override
	protected void onSeamothSlotChanged(int slot) {
		if (this.seamoth != null) {
			this.seamoth.syncPowerCell();
		}
	}

}
