package meldexun.better_diving.client.gui.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import meldexun.better_diving.BetterDiving;
import meldexun.better_diving.capability.inventory.item.CapabilityItemHandlerSeamoth;
import meldexun.better_diving.entity.EntitySeamoth;
import meldexun.better_diving.init.BetterDivingEntities;
import meldexun.better_diving.init.BetterDivingItems;
import meldexun.better_diving.inventory.container.ContainerSeamoth;
import meldexun.better_diving.item.ItemPowerCell;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import org.joml.Math;
import org.joml.Quaternionf;

public class ScreenSeamoth extends AbstractContainerScreen<ContainerSeamoth> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(BetterDiving.MOD_ID,
                    "textures/gui/seamoth_container.png");

    private final EntitySeamoth seamoth = new EntitySeamoth(
            BetterDivingEntities.SEAMOTH.get(), Minecraft.getInstance().level);

    public ScreenSeamoth(ContainerSeamoth screenContainer, Inventory inv,
                         Component titleIn) {
        super(screenContainer, inv, titleIn);
    }

    /**
     * Copied from
     * {@link InventoryScreen#renderEntityInInventory(GuiGraphics, int, int, int, Quaternionf, Quaternionf, LivingEntity)} (int, int, int, float, float, LivingEntity)}
     */
    public static void renderEntityInInventory(GuiGraphics guiGraphics,
                                               int posX,
                                               int posY,
                                               int scale
            , float mouseX, float mouseY, Entity entity) {
        float f = (float) java.lang.Math.atan((mouseX / 40.0F));
        float f1 = (float) java.lang.Math.atan((mouseY / 40.0F));
        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();
        poseStack.translate((float) posX, (float) posY, 1050.0F);
        poseStack.scale(1.0F, 1.0F, -1.0F);
        poseStack.translate(0.0D, 0.0D, 1000.0D);
        poseStack.scale((float) scale, (float) scale, (float) scale);
        Quaternionf quaternion = Axis.ZP.rotationDegrees(180F);
        Quaternionf quaternion1 = Axis.XP.rotationDegrees(f1 * 20.0F);
        quaternion.mul(quaternion1);
        poseStack.mulPose(quaternion);
        float f3 = entity.yRotO;
        float f4 = entity.xRotO;
        entity.yRotO = 180.0F + f * 40.0F;
        entity.xRotO = -f1 * 20.0F;
        EntityRenderDispatcher entityrenderermanager = Minecraft.getInstance()
                .getEntityRenderDispatcher();
        quaternion1.conjugate();
        entityrenderermanager.overrideCameraOrientation(quaternion1);
        entityrenderermanager.setRenderShadow(false);
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance()
                .renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> {
            entityrenderermanager.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F,
                    poseStack, bufferSource, 15728880);
        });
        bufferSource.endBatch();
        entityrenderermanager.setRenderShadow(true);
        entity.yRotO = f3;
        entity.xRotO = f4;
        poseStack.popPose();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY,
                       float partialTicks) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int x, int y) {
        super.renderLabels(guiGraphics, x, y);
        ItemStack powerCell = this.menu.getSlot(36).getItem();
        if (powerCell.getItem() instanceof ItemPowerCell) {
            IEnergyStorage ienergy =
                    powerCell.getCapability(ForgeCapabilities.ENERGY)
                            .orElseThrow(NullPointerException::new);
            int energy =
                    (int) Math.ceil(
                            (double) ienergy.getEnergyStored() / (double) ienergy.getMaxEnergyStored() * 100.0D);
            // this.font.draw(matrixStack, "Energy: " + energy + "%", 8, 16,
            // 		4210752);
            // this.font.drawInBatch("Energy: " + energy + "%")
            guiGraphics.drawString(this.font, "Energy: " + energy +
                    "%", 8, 16, 4210752);
        } else {
            guiGraphics.drawString(this.font, "No power cell", 8, 16, 4210752);
            // this.font.draw(matrixStack, "No power cell", 8, 16, 4210752);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int x, int y) {
        GlStateManager._clearColor(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindForSetup(TEXTURE);
        guiGraphics.blit(TEXTURE, (this.width - this.imageWidth) / 2,
                (this.height - this.imageHeight) / 2, 0, 0, this.imageWidth,
                this.imageHeight);
        // not drawing seamoth preview here to make space for extended seamoth
        // inv
        // this.drawEntity(guiGraphics, this.seamoth,
        //         (this.width - this.imageWidth) / 2 + 118,
        //         (this.height - this.imageHeight) / 2 + 60, 16, x, y);
        // draws blind/cover on rows that don't have a storage module
        // or on storage modules with corresponding non-empty inv row
        int firstModuleSlotOffset = ContainerSeamoth.FIRST_MODULE_SLOT_OFFSET;
        int invCoverStart = -1;
        int invCoverSpan = 0;
        int moduleCoverStart = -1;
        int moduleCoverSpan = 0;
        for (int i = 0; i < 4; i++){
            if (!getMenu().getSlot(firstModuleSlotOffset + i).getItem().is(BetterDivingItems.VEHICLE_STORAGE_MODULE.get())) {
                if (invCoverStart == -1) {
                    invCoverStart = i;
                }
                invCoverSpan += 1;

                if (moduleCoverStart >= 0) {
                    drawModuleCover(guiGraphics, moduleCoverStart, moduleCoverSpan);
                }
                moduleCoverStart = -1;
                moduleCoverSpan = 0;
            }
            else {
                if (invCoverStart >= 0) {
                    drawStorageCover(guiGraphics, invCoverStart, invCoverSpan);
                }
                invCoverStart = -1;
                invCoverSpan = 0;

                if (getMenu().invRowHasItem(i)) {
                    if (moduleCoverStart == -1) {
                        moduleCoverStart = i;
                    }
                    moduleCoverSpan += 1;
                }
                else {
                    if (moduleCoverStart >= 0) {
                        drawModuleCover(guiGraphics, moduleCoverStart, moduleCoverSpan);
                    }
                    moduleCoverStart = -1;
                    moduleCoverSpan = 0;
                }
            }
        }
        // runaway case of should draw ... till last row
        if (invCoverStart >= 0) {
            drawStorageCover(guiGraphics, invCoverStart, invCoverSpan);
        }
        if (moduleCoverStart >= 0) {
            drawModuleCover(guiGraphics, moduleCoverStart, moduleCoverSpan);
        }
    }

    private void drawStorageCover(GuiGraphics guiGraphics, int start,
                                  int span) {
        guiGraphics.fill(
                this.leftPos + 79,
                this.topPos + 7 + 18 * start,
                this.leftPos + 79 + 18 * CapabilityItemHandlerSeamoth.SLOT_PER_STORAGE_MODULE,
                this.topPos + 7 + 18 * (start + span),
                FastColor.ARGB32.color(127, 31, 31, 31));
    }

    private void drawModuleCover(GuiGraphics guiGraphics, int start,
                                  int span) {
        guiGraphics.fill(
                this.leftPos + 61,
                this.topPos + 7 + 18 * start,
                this.leftPos + 61 + 18,
                this.topPos + 7 + 18 * (start + span),
                FastColor.ARGB32.color(200, 127, 63, 63));
    }

    private void drawEntity(GuiGraphics guiGraphics, Entity entity, int x,
                            int y, int scale,
                            float mouseX, float mouseY) {
        renderEntityInInventory(guiGraphics, x, y, scale, x - mouseX,
                y - entity.getEyeHeight() * scale - mouseY, entity);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

}
