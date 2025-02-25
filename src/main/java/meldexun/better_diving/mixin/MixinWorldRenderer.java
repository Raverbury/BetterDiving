package meldexun.better_diving.mixin;

import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import meldexun.better_diving.config.BetterDivingConfig;
import net.minecraft.client.Minecraft;

@SuppressWarnings("deprecation")
@Mixin(LevelRenderer.class)
public class MixinWorldRenderer {

	@Unique
	private static float moonR;
	@Unique
	private static float moonG;
	@Unique
	private static float moonB;
	@Unique
	private static float moonA;

	@Unique
	private static double seaLevel() {
		double seaLevel;
		if (BetterDivingConfig.CLIENT_CONFIG.seaLevelOverride.get()) {
			seaLevel = BetterDivingConfig.CLIENT_CONFIG.seaLevel.get();
		} else {
			Minecraft mc = Minecraft.getInstance();
			seaLevel = mc.level.getSeaLevel();
		}
		return seaLevel;
	}

	@Unique
	private static double maxSunMoonAngle() {
		Minecraft mc = Minecraft.getInstance();
		double d1 = (mc.options.renderDistance().get() - 1) * 16.0D;
		double d2 = seaLevel() - mc.gameRenderer.getMainCamera().getPosition().y();
		return Math.toDegrees(Math.atan2(d1, d2) - Math.atan2(7.5D, 100.0D));
	}

}
