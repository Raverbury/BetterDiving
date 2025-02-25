package meldexun.better_diving.network.packet.server;

import java.util.function.Supplier;

import meldexun.better_diving.client.ClientBetterDiving;
import meldexun.better_diving.network.packet.IPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

public class SPacketSyncOxygen implements IPacket {

	private int oxygen;

	public SPacketSyncOxygen() {

	}

	public SPacketSyncOxygen(int oxygen) {
		this.oxygen = oxygen;
	}

	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.oxygen);
	}

	@Override
	public void decode(FriendlyByteBuf buffer) {
		this.oxygen = buffer.readInt();
	}

	@Override
	public boolean handle(Supplier<NetworkEvent.Context> ctxSupplier) {
		Player player = ClientBetterDiving.getPlayer();
		// player.getCapability(CapabilityOxygenProvider.OXYGEN).ifPresent(cap -> cap.setOxygen(this.oxygen));
		return true;
	}

}
