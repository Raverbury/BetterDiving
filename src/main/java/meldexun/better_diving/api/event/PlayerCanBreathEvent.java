package meldexun.better_diving.api.event;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

/**
 * Use this event to allow/disallow a player to breath.<br>
 * For example could be used to disallow a player to breath while in a moon dimension.
 */
@Deprecated
public class PlayerCanBreathEvent extends Event {

	private final Player player;
	private boolean canBreath;

	public PlayerCanBreathEvent(Player player, boolean canBreath) {
		this.player = player;
		this.canBreath = canBreath;
	}

	public Player getPlayer() {
		return this.player;
	}

	public boolean canBreath() {
		return this.canBreath;
	}

	public void setCanBreath(boolean canBreath) {
		this.canBreath = canBreath;
	}

}
