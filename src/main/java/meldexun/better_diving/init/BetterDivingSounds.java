package meldexun.better_diving.init;

import meldexun.better_diving.BetterDiving;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BetterDivingSounds {

    private static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(
            ForgeRegistries.SOUND_EVENTS, BetterDiving.MOD_ID);

    public static final RegistryObject<SoundEvent> SEAMOTH_ENTER =
            SOUNDS.register("seamoth_enter",
                    () -> SoundEvent.createVariableRangeEvent(
                            new ResourceLocation(BetterDiving.MOD_ID,
                                    "seamoth_enter")));
    public static final RegistryObject<SoundEvent> SEAMOTH_START =
            SOUNDS.register("seamoth_start",
                    () -> SoundEvent.createVariableRangeEvent(
                            new ResourceLocation(BetterDiving.MOD_ID,
                                    "seamoth_start")));
    public static final RegistryObject<SoundEvent> SEAMOTH_ENGINE_LOOP =
            SOUNDS.register("seamoth_engine_loop",
                    () -> SoundEvent.createVariableRangeEvent(
                            new ResourceLocation(BetterDiving.MOD_ID,
                                    "seamoth_engine_loop")));

    public static void registerSounds() {
        SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

}
