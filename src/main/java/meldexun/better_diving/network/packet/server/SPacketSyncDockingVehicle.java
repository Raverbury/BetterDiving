package meldexun.better_diving.network.packet.server;

import meldexun.better_diving.api.entity.EntityBetterDivingVehicle;
import meldexun.better_diving.client.ClientBetterDiving;
import meldexun.better_diving.network.packet.IPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SPacketSyncDockingVehicle implements IPacket {

    private int id;
    private float x;
    private float y;
    private float z;
    private float yaw;
    private float pitch;

    public SPacketSyncDockingVehicle() {

    }

    public SPacketSyncDockingVehicle(EntityBetterDivingVehicle vehicle, float x,
                                     float y, float z,
                                     float yaw,
                                     float pitch) {
        this.id = vehicle.getId();
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(id);
        buffer.writeFloat(x);
        buffer.writeFloat(y);
        buffer.writeFloat(z);
        buffer.writeFloat(yaw);
        buffer.writeFloat(pitch);
    }

    @Override
    public void decode(FriendlyByteBuf buffer) {
        id = buffer.readInt();
        x = buffer.readFloat();
        y = buffer.readFloat();
        z = buffer.readFloat();
        yaw = buffer.readFloat();
        pitch = buffer.readFloat();
    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> ctxSupplier) {
        ctxSupplier.get().enqueueWork(() -> {
            Level level = ClientBetterDiving.getLevel();
            Entity entity = level.getEntity(id);
            if (entity instanceof EntityBetterDivingVehicle) {
                entity.setYRot(yaw);
                entity.setXRot(pitch);
                entity.setPos(x, y, z);
            }
        });
        return true;
    }
}
