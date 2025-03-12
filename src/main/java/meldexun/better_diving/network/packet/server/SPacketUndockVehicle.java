package meldexun.better_diving.network.packet.server;

import meldexun.better_diving.api.entity.EntityBetterDivingVehicle;
import meldexun.better_diving.client.ClientBetterDiving;
import meldexun.better_diving.network.packet.IPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/*
proof:
[15:13:13] [Render thread/INFO] [me.je.li.lo.PluginCaller/]: Sending Runtime took 870.2 ﾎｼs
[15:13:13] [Render thread/INFO] [me.je.co.ut.LoggedTimer/]: Starting JEI took 3.435 s
[15:13:13] [Render thread/INFO] [Jade/]: Received config from the server:
[15:13:13] [Render thread/INFO] [better_diving/]: ++++++++Add passenger <--- uh huh
[15:13:13] [Render thread/INFO] [better_diving/]: -----------------Start docking
[15:13:13] [Server thread/INFO] [better_diving/]: -----------------Start docking
[15:13:14] [Render thread/INFO] [better_diving/]: ++++++++Add passenger <--- uhhhhhh???
[15:13:14] [Render thread/INFO] [minecraft/AdvancementList]: Loaded 77 advancements
[15:13:15] [Render thread/INFO] [better_diving/]: ~~~~~~~~~~~~~~~~Clear <--- dear neptune
[15:13:18] [Server thread/INFO] [minecraft/IntegratedServer]: Saving and pausing game...
 */

/**
 * Because Entity#addPassenger fires twice on client when joining level while
 * previously still riding an entity for some reason, leading to desync...
 * Thanks Mojank. Check this class file for more details.
 */
public class SPacketUndockVehicle implements IPacket {

    private int vehicleId;

    public SPacketUndockVehicle() {
    }

    public SPacketUndockVehicle(EntityBetterDivingVehicle vehicle) {
        vehicleId = vehicle.getId();
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.vehicleId);
    }

    @Override
    public void decode(FriendlyByteBuf buffer) {
        this.vehicleId = buffer.readInt();
    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> ctxSupplier) {
        ctxSupplier.get().enqueueWork(() -> {
            Level level = ClientBetterDiving.getLevel();
            Entity entity = level.getEntity(this.vehicleId);
            if (entity instanceof EntityBetterDivingVehicle) {
                ((EntityBetterDivingVehicle) entity).releaseDocking();
            }
        });
        return true;
    }
}
