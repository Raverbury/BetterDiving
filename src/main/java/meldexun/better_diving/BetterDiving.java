package meldexun.better_diving;

import meldexun.better_diving.init.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import meldexun.better_diving.api.BetterDivingModules;
import meldexun.better_diving.config.BetterDivingConfig;
import meldexun.better_diving.oxygenprovider.DivingGearManager;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(BetterDiving.MOD_ID)
public class BetterDiving {

	public static final String MOD_ID = "better_diving";

	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	private static final String NETWORK_VERSION = "1.0.0";
	public static final SimpleChannel NETWORK =
			NetworkRegistry.newSimpleChannel(new ResourceLocation(MOD_ID, "main"),
					() -> NETWORK_VERSION, NETWORK_VERSION::equals, NETWORK_VERSION::equals);

	public BetterDiving() {
		ModLoadingContext.get().registerConfig(Type.CLIENT, BetterDivingConfig.CLIENT_SPEC);
		ModLoadingContext.get().registerConfig(Type.SERVER, BetterDivingConfig.SERVER_SPEC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::updateConfig);
		BetterDivingBlocks.registerBlocks();
		BetterDivingItems.registerItems();
		BetterDivingEntities.registerEntities();
		BetterDivingBlockEntities.registerBlockEntities();
		BetterDivingContainers.registerContainers();
		BetterDivingSounds.registerSounds();
		BetterDivingFeatures.registerFeatures();
		BetterDivingCreativeTab.registerCreativeTabs();
	}

	private void setup(FMLCommonSetupEvent event) {
		BetterDivingCapabilities.registerCapabilities();
		BetterDivingPackets.registerPackets();
	}

	private void updateConfig(ModConfigEvent event) {
		if (event.getConfig().getModId().equals(MOD_ID)) {
			DivingGearManager.reloadConfigs();
			BetterDivingModules.breakSpeedChanges =
					false && BetterDivingConfig.SERVER_CONFIG.breakSpeedChanges.get();
			BetterDivingModules.movementChanges = false && BetterDivingConfig.SERVER_CONFIG.movementChanges.get();
			BetterDivingModules.oxygenChanges = false && BetterDivingConfig.SERVER_CONFIG.oxygenChanges.get();
		}
	}

	public enum Vehicle {
		Seamoth,
		Other,
	}

}
