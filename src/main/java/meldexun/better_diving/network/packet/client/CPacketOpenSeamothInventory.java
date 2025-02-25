package meldexun.better_diving.network.packet.client;

import java.util.function.Supplier;

import meldexun.better_diving.entity.EntitySeamoth;
import meldexun.better_diving.inventory.container.ContainerSeamothEntity;
import meldexun.better_diving.network.packet.IPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

public class CPacketOpenSeamothInventory implements IPacket {

	@Override
	public void encode(FriendlyByteBuf buffer) {

	}

	@Override
	public void decode(FriendlyByteBuf buffer) {

	}

	@Override
	public boolean handle(Supplier<NetworkEvent.Context> ctxSupplier) {
		ctxSupplier.get().enqueueWork(() -> {
			Player player = ctxSupplier.get().getSender();
			Entity entity = player.getVehicle();

			if (entity instanceof EntitySeamoth) {
				player.openMenu(new SimpleMenuProvider((id, playerInv, player1) -> {
					return new ContainerSeamothEntity(id, playerInv, (EntitySeamoth) entity);
				}, Component.literal("Seamoth")));
			}
		});
		return true;
	}

}
