package meldexun.better_diving.network.packet.server;

import java.util.function.Supplier;

import meldexun.better_diving.client.ClientBetterDiving;
import meldexun.better_diving.entity.EntityPowerCellPoweredVehicle;
import meldexun.better_diving.item.ItemEnergyStorage;
import meldexun.better_diving.network.packet.IPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkEvent;

public class SPacketSyncPowerCellVehicleEnergy implements IPacket {

	private int entityId;
	private int energy;

	public SPacketSyncPowerCellVehicleEnergy() {

	}

	public SPacketSyncPowerCellVehicleEnergy(EntityPowerCellPoweredVehicle vehicle) {
		this.entityId = vehicle.getId();
		LazyOptional<IItemHandler> optionalItemHandler =
				vehicle.getCapability(ForgeCapabilities.ITEM_HANDLER);
		if (optionalItemHandler.isPresent()) {
			IItemHandler itemHandler = optionalItemHandler.orElseThrow(NullPointerException::new);
			this.energy = ItemEnergyStorage.getEnergy(itemHandler.getStackInSlot(0));
		}
	}

	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.entityId);
		buffer.writeInt(this.energy);
	}

	@Override
	public void decode(FriendlyByteBuf buffer) {
		this.entityId = buffer.readInt();
		this.energy = buffer.readInt();
	}

	@Override
	public boolean handle(Supplier<NetworkEvent.Context> ctxSupplier) {
		ctxSupplier.get().enqueueWork(() -> {
			Level world = ClientBetterDiving.getLevel();
			Entity entity = world.getEntity(this.entityId);
			if (entity instanceof EntityPowerCellPoweredVehicle) {
				EntityPowerCellPoweredVehicle vehicle =
						(EntityPowerCellPoweredVehicle) entity;

				vehicle.setEnergy(this.energy);
			}
		});
		return true;
	}

}
