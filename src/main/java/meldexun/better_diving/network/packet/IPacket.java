package meldexun.better_diving.network.packet;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public interface IPacket {

	void encode(FriendlyByteBuf buffer);

	void decode(FriendlyByteBuf buffer);

	boolean handle(Supplier<NetworkEvent.Context> ctxSupplier);

}
