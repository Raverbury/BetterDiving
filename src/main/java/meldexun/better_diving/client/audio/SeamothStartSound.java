package meldexun.better_diving.client.audio;

import meldexun.better_diving.entity.EntitySeamoth;
import meldexun.better_diving.init.BetterDivingSounds;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Math;

@OnlyIn(Dist.CLIENT)
public class SeamothStartSound extends AbstractTickableSoundInstance {

	private EntitySeamoth seamoth;
	private boolean stop;
	private int tick = 10;

	public SeamothStartSound(EntitySeamoth seamoth) {
		super(BetterDivingSounds.SEAMOTH_START.get(),
				seamoth.getSoundSource(), seamoth.level().getRandom());
		this.seamoth = seamoth;
		this.volume = 1.0F;
		this.pitch = 1.0F;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void tick() {
		if (this.seamoth.isRemoved() || this.tick < 0) {
			this.stop();
		} else {
			this.x = this.seamoth.getX();
			this.y = this.seamoth.getY();
			this.z = this.seamoth.getZ();

			if (!this.stop && (!this.seamoth.isPlayerSteering() || !this.seamoth.hasEnergy())) {
				this.stop = true;
			}
			if (this.stop) {
				this.tick--;
			}

			this.volume = 0.25F * Math.clamp(this.tick / 10.0F, 0.0F, 1.0F);
		}
	}

	public int getTick() {
		return this.tick;
	}

	public void setTick(int tick) {
		this.tick = tick;
	}

}
