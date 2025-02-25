package meldexun.better_diving.init;

import java.util.function.Supplier;

import meldexun.better_diving.BetterDiving;
import meldexun.better_diving.network.packet.IPacket;
import meldexun.better_diving.network.packet.client.CPacketOpenSeamothInventory;
import meldexun.better_diving.network.packet.client.CPacketSyncSeamothInput;
import meldexun.better_diving.network.packet.server.SPacketSyncOxygen;
import meldexun.better_diving.network.packet.server.SPacketSyncSeamothEnergy;
import meldexun.better_diving.network.packet.server.SPacketSyncSeamothPowerCell;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.simple.SimpleChannel;

public class BetterDivingPackets {

	private static int id = 1;

	public static void registerPackets() {
		registerPacket(SPacketSyncOxygen.class, NetworkDirection.PLAY_TO_CLIENT,
				SPacketSyncOxygen::new);
		registerPacket(CPacketSyncSeamothInput.class, NetworkDirection.PLAY_TO_SERVER, CPacketSyncSeamothInput::new);
		registerPacket(SPacketSyncSeamothPowerCell.class, NetworkDirection.PLAY_TO_CLIENT, SPacketSyncSeamothPowerCell::new);
		registerPacket(SPacketSyncSeamothEnergy.class, NetworkDirection.PLAY_TO_CLIENT, SPacketSyncSeamothEnergy::new);
		registerPacket(CPacketOpenSeamothInventory.class, NetworkDirection.PLAY_TO_SERVER, CPacketOpenSeamothInventory::new);
	}

	private static <T extends IPacket> void registerPacket(Class<T> packetClass, NetworkDirection direction, Supplier<T> packetSupplier) {
		SimpleChannel.MessageBuilder<T> builder = BetterDiving.NETWORK.messageBuilder(packetClass,
				id++,	direction);
		builder.encoder(IPacket::encode);
		builder.decoder(buffer -> {
			T packet = packetSupplier.get();
			packet.decode(buffer);
			return packet;
		});
		builder.consumerNetworkThread(IPacket::handle);
		builder.add();
	}

}
