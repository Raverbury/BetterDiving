package meldexun.better_diving.api.event;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

public class PlayerSwimSpeedEvent extends Event {

	private final Player player;
	private final double originalSwimSpeed;
	private double newSwimSpeed;

	public PlayerSwimSpeedEvent(Player player, double originalSwimSpeed) {
		this.player = player;
		this.originalSwimSpeed = originalSwimSpeed;
		this.newSwimSpeed = originalSwimSpeed;
	}

	public Player getPlayer() {
		return this.player;
	}

	public double getOriginalSwimSpeed() {
		return this.originalSwimSpeed;
	}

	public double getNewSwimSpeed() {
		return this.newSwimSpeed;
	}

	public void setNewSwimSpeed(double newSwimSpeed) {
		this.newSwimSpeed = newSwimSpeed;
	}

}
