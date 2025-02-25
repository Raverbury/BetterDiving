package meldexun.better_diving.network.packet.client;

import meldexun.better_diving.entity.EntitySeamoth;
import meldexun.better_diving.network.packet.IPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CPacketSyncSeamothInput implements IPacket {

    private int entityId;
    private boolean inputForward;
    private boolean inputBack;
    private boolean inputRight;
    private boolean inputLeft;
    private boolean inputUp;
    private boolean inputDown;
    private float yaw;
    private float pitch;

    public CPacketSyncSeamothInput() {

    }

    public CPacketSyncSeamothInput(EntitySeamoth seamoth) {
        this.entityId = seamoth.getId();
        this.inputForward = seamoth.inputForward;
        this.inputBack = seamoth.inputBack;
        this.inputRight = seamoth.inputRight;
        this.inputLeft = seamoth.inputLeft;
        this.inputUp = seamoth.inputUp;
        this.inputDown = seamoth.inputDown;
        this.yaw = seamoth.getYRot();
        this.pitch = seamoth.getXRot();
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeBoolean(this.inputForward);
        buffer.writeBoolean(this.inputBack);
        buffer.writeBoolean(this.inputRight);
        buffer.writeBoolean(this.inputLeft);
        buffer.writeBoolean(this.inputUp);
        buffer.writeBoolean(this.inputDown);
        buffer.writeFloat(this.yaw);
        buffer.writeFloat(this.pitch);
    }

    @Override
    public void decode(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
        this.inputForward = buffer.readBoolean();
        this.inputBack = buffer.readBoolean();
        this.inputRight = buffer.readBoolean();
        this.inputLeft = buffer.readBoolean();
        this.inputUp = buffer.readBoolean();
        this.inputDown = buffer.readBoolean();
        this.yaw = buffer.readFloat();
        this.pitch = buffer.readFloat();
    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> ctxSupplier) {
        ctxSupplier.get().enqueueWork(() -> {
            Player player = ctxSupplier.get().getSender();
            Entity entity = player.level().getEntity(this.entityId);
            if (entity instanceof EntitySeamoth) {
                EntitySeamoth seamoth = (EntitySeamoth) entity;

                seamoth.inputForward = this.inputForward;
                seamoth.inputBack = this.inputBack;
                seamoth.inputRight = this.inputRight;
                seamoth.inputLeft = this.inputLeft;
                seamoth.inputUp = this.inputUp;
                seamoth.inputDown = this.inputDown;
                seamoth.setYRot(this.yaw);
                seamoth.setXRot(this.pitch);
            }
        });
        return true;
    }

}
