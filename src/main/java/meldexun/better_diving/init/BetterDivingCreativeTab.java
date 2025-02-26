package meldexun.better_diving.init;

import meldexun.better_diving.BetterDiving;
import meldexun.better_diving.api.capability.IEnergyStorageExtended;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class BetterDivingCreativeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB_DEFERRED_REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB,
                    BetterDiving.MOD_ID);
    public static final RegistryObject<CreativeModeTab> CREATIVE_TAB =
            CREATIVE_MODE_TAB_DEFERRED_REGISTER.register("better_diving",
                    () -> CreativeModeTab.builder().title(Component.literal(
                                    "Better" +
                                            " Diving")).icon(() -> new ItemStack(
                                    BetterDivingItems.SEAMOTH.get()))
                            .displayItems((params, output) -> {
                                // the rest
                                for (var item :
                                        BetterDivingItems.ITEMS.getEntries()) {
                                    output.accept(item.get());
                                }
                                for (var block :
                                        BetterDivingBlocks.BLOCKS.getEntries()) {
                                    output.accept(block.get());
                                }
                                // custom powercell with energy cap, 0 energy
                                ItemStack powerCellStack = new ItemStack(
                                        BetterDivingItems.POWER_CELL.get());
                                powerCellStack.getCapability(
                                                ForgeCapabilities.ENERGY)
                                        .ifPresent(c -> {
                                            ((IEnergyStorageExtended) c).setEnergy(
                                                    0);
                                        });
                                output.accept(powerCellStack);
                                // custom seamoth with item cap and power
                                // cell inside
                                ItemStack seamothStack =
                                        new ItemStack(
                                                BetterDivingItems.SEAMOTH.get());
                                seamothStack.getCapability(
                                                ForgeCapabilities.ITEM_HANDLER)
                                        .ifPresent(c -> {
                                            BetterDiving.LOGGER.info(
                                                    "Creativetab seamoth has " +
                                                            "cap");
                                            c.insertItem(
                                                    0, new ItemStack(
                                                            BetterDivingItems.POWER_CELL.get()), false);
                                        });
                                output.accept(seamothStack);
                            }).build());

    public static void registerCreativeTabs() {
        CREATIVE_MODE_TAB_DEFERRED_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
