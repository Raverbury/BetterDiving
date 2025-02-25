package meldexun.better_diving.network.packet.server;

import java.util.function.Supplier;

import meldexun.better_diving.client.ClientBetterDiving;
import meldexun.better_diving.entity.EntitySeamoth;
import meldexun.better_diving.network.packet.IPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkEvent;

public class SPacketSyncSeamothPowerCell implements IPacket {

	private int entityId;
	private ItemStack powerCell = ItemStack.EMPTY;

	public SPacketSyncSeamothPowerCell() {

	}

	public SPacketSyncSeamothPowerCell(EntitySeamoth seamoth) {
		this.entityId = seamoth.getId();
		LazyOptional<IItemHandler> optionalItemHandler =
				seamoth.getCapability(ForgeCapabilities.ITEM_HANDLER);
		if (optionalItemHandler.isPresent()) {
			IItemHandler itemHandler = optionalItemHandler.orElseThrow(NullPointerException::new);
			this.powerCell = itemHandler.getStackInSlot(0);
		}
	}

	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.entityId);
		buffer.writeItemStack(this.powerCell, false);
	}

	@Override
	public void decode(FriendlyByteBuf buffer) {
		this.entityId = buffer.readInt();
		this.powerCell = buffer.readItem();
	}

	@Override
	public boolean handle(Supplier<NetworkEvent.Context> ctxSupplier) {
		ctxSupplier.get().enqueueWork(() -> {
			Level world = ClientBetterDiving.getLevel();
			Entity entity = world.getEntity(this.entityId);
			if (entity instanceof EntitySeamoth) {
				EntitySeamoth seamoth = (EntitySeamoth) entity;

				seamoth.setPowerCell(this.powerCell);
			}
		});
		return true;
	}

}
