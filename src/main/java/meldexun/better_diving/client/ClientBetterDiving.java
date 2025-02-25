package meldexun.better_diving.client;

import meldexun.better_diving.BetterDiving;
import meldexun.better_diving.client.gui.screen.ScreenSeamoth;
import meldexun.better_diving.client.renderer.entity.RenderSeamoth;
import meldexun.better_diving.init.BetterDivingContainers;
import meldexun.better_diving.init.BetterDivingEntities;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = BetterDiving.MOD_ID, value = Dist.CLIENT, bus = Bus.MOD)
public class ClientBetterDiving {

    public static final KeyMapping KEY_BIND_DESCEND = new KeyMapping("Descend",
            GLFW.GLFW_KEY_C,
            BetterDiving.MOD_ID);

    @SuppressWarnings("resource")
    public static Player getPlayer() {
        return Minecraft.getInstance().player;
    }

    @SuppressWarnings("resource")
    public static Level getLevel() {
        return Minecraft.getInstance().level;
    }

    @SubscribeEvent
    public static void onFMLClientSetupEvent(FMLClientSetupEvent event) {
        event.enqueueWork(
                () -> {
                    MenuScreens.register(
                            BetterDivingContainers.SEAMOTH_ITEM.get(),
                            ScreenSeamoth::new);
                    MenuScreens.register(
                            BetterDivingContainers.SEAMOTH_ENTITY.get(),
                            ScreenSeamoth::new);
                }
        );
    }

    @SubscribeEvent
    public static void onRegisterRendererEvent(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(BetterDivingEntities.SEAMOTH.get(),
                RenderSeamoth::new);

    }

    @SubscribeEvent
    public static void onRegisterKeyBindingEvent(RegisterKeyMappingsEvent event) {
        event.register(KEY_BIND_DESCEND);
    }
}
