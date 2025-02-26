package meldexun.better_diving.init;

import meldexun.better_diving.BetterDiving;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.common.util.ForgeSoundType;
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
    public static final RegistryObject<SoundEvent> SEAMOTH_WELCOME =
            SOUNDS.register("seamoth_welcome",
                    () -> SoundEvent.createVariableRangeEvent(
                            new ResourceLocation(BetterDiving.MOD_ID,
                                    "seamoth_welcome")));
    public static final RegistryObject<SoundEvent> SEAMOTH_NO_POWER =
            SOUNDS.register("seamoth_no_power",
                    () -> SoundEvent.createVariableRangeEvent(
                            new ResourceLocation(BetterDiving.MOD_ID,
                                    "seamoth_no_power")));
    public static final RegistryObject<SoundEvent> OUTCROP_HIT =
            SOUNDS.register("outcrop_hit",
                    () -> SoundEvent.createVariableRangeEvent(
                            new ResourceLocation(BetterDiving.MOD_ID,
                                    "outcrop_hit")));
    public static final RegistryObject<SoundEvent> OUTCROP_BREAK =
            SOUNDS.register("outcrop_break",
                    () -> SoundEvent.createVariableRangeEvent(
                            new ResourceLocation(BetterDiving.MOD_ID,
                                    "outcrop_break")));
    public static final RegistryObject<SoundEvent> ADVANCEMENT_SEAMOTH =
            SOUNDS.register("advancement_seamoth",
                    () -> SoundEvent.createVariableRangeEvent(
                            new ResourceLocation(BetterDiving.MOD_ID,
                                    "advancement_seamoth")));

    // stop moving this you damn autoformat
    public static final SoundType OUTCROP_SOUNDS = new ForgeSoundType(1f, 1f,
            OUTCROP_BREAK, () -> SoundEvents.STONE_STEP,
            () -> SoundEvents.STONE_PLACE, OUTCROP_HIT,
            () -> SoundEvents.SAND_FALL);

    public static void registerSounds() {
        SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

}
