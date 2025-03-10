package meldexun.better_diving.client;

import meldexun.better_diving.BetterDiving;
import meldexun.better_diving.client.gui.screen.ScreenSeamoth;
import meldexun.better_diving.client.model.PerspectiveBakedModel;
import meldexun.better_diving.client.renderer.entity.RenderSeamoth;
import meldexun.better_diving.init.BetterDivingContainers;
import meldexun.better_diving.init.BetterDivingEntities;
import meldexun.better_diving.init.BetterDivingItems;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Map;

// Render custom model for item separately in world/hand referenced from
// TouhouLittleMaid
// at https://github.com/TartaricAcid/TouhouLittleMaid/blob/1c5dcfb4eece68c2ebd49e11b054021965340234/src/main/java/com/github/tartaricacid/touhoulittlemaid/client/init/InitSpecialItemRender.java

@Mod.EventBusSubscriber(modid = BetterDiving.MOD_ID, value = Dist.CLIENT, bus = Bus.MOD)
public class ClientBetterDiving {

    private static final List<Pair<ModelResourceLocation,
            ModelResourceLocation>> MODELS = Lists.newArrayList();

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
    public static void onRegisterEvent(RegisterEvent event) {
        if (event.getRegistryKey().equals(Registries.ITEM)) {
            addModuleModel(BetterDivingItems.VEHICLE_ENGINE_EFFICIENCY_MODULE.get());
            addModuleModel(BetterDivingItems.VEHICLE_STORAGE_MODULE.get());
        }
    }

    private static void addModuleModel(Item item) {
        ResourceLocation res = ForgeRegistries.ITEMS.getKey(item);
        if (res != null) {
            ModelResourceLocation raw = new ModelResourceLocation(res,
                    "inventory");
            ModelResourceLocation inHand =
                    new ModelResourceLocation(res.getNamespace(),
                            "vehicle_module", "inventory");
            MODELS.add(Pair.of(raw, inHand));
        }
    }

    @SubscribeEvent
    public static void onBakedModel(ModelEvent.BakingCompleted event) {
        Map<ResourceLocation, BakedModel> registry = event.getModelBakery().getBakedTopLevelModels();
        for (Pair<ModelResourceLocation, ModelResourceLocation> pair : MODELS) {
            PerspectiveBakedModel model =
                    new PerspectiveBakedModel(registry.get(pair.getLeft()), registry.get(pair.getRight()));
            registry.put(pair.getLeft(), model);
        }
    }


    @SubscribeEvent
    public static void onRegisterModel(ModelEvent.RegisterAdditional event) {
        MODELS.forEach((pair) -> event.register(pair.getRight()));
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
