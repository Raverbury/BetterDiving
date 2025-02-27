package meldexun.better_diving.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.PoseStack;
import meldexun.better_diving.BetterDiving;
import meldexun.better_diving.client.util.BetterDivingGuiHelper;
import meldexun.better_diving.config.BetterDivingConfig;
import meldexun.better_diving.entity.EntitySeamoth;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import org.joml.Math;
import org.lwjgl.opengl.GL11;

public class GuiSeamoth {

    @SuppressWarnings("deprecation")
    public static void render(PoseStack poseStack) {
        Minecraft mc = Minecraft.getInstance();
        TextureManager textureManager = mc.getTextureManager();
        Font font = mc.font;
        EntitySeamoth seamoth = ((EntitySeamoth) mc.player.getVehicle());

        boolean blend = GL11.glGetBoolean(GL11.GL_BLEND);
        GlStateManager._enableBlend();
        GlStateManager._blendFuncSeparate(GL11.GL_SRC_ALPHA,
                GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        // GlStateManager.al(GL11.GL_GREATER, 0.00390625F);

        int width = 81;
        int height = 37;
        int offsetX = BetterDivingGuiHelper.getAnchorX(width,
                BetterDivingConfig.CLIENT_CONFIG.seamothGuiAnchor.get(),
                BetterDivingConfig.CLIENT_CONFIG.seamothGuiOffsetX.get());
        int offsetY = BetterDivingGuiHelper.getAnchorY(height,
                BetterDivingConfig.CLIENT_CONFIG.seamothGuiAnchor.get(),
                BetterDivingConfig.CLIENT_CONFIG.seamothGuiOffsetY.get());

        // textureManager.bindForSetup();
        BetterDivingGuiHelper.drawTexture(new ResourceLocation(BetterDiving.MOD_ID,
                        "textures/gui/seamoth_background.png"), offsetX,
                offsetY, 0.0D, 0.0D, width,
                height, width / 128.0D, height / 64.0D);

        int energy =
                (int) Math.ceil(
                        (double) seamoth.getEnergy() / (double) seamoth.getEnergyCapacity() * 100.0D);
        int health = 100;
        int temperature =
                Math.round(20.0F * mc.level.getBiome(mc.player.blockPosition())
                        .get().getTemperature(mc.player.blockPosition()));

        String s1 = Integer.toString(energy);
        draw(font, s1,
                offsetX + 67f - font.width(s1) / 2f, offsetY + 5f,
                0xFFFFFF, poseStack);
        String s2 = Integer.toString(health);
        draw(font, s2,
                offsetX + 28f - font.width(s2) / 2f, offsetY + 13f,
                0xFFFFFF, poseStack);

        String s3 = Integer.toString(temperature);
        draw(font, s3,
                offsetX + 62 - font.width(s3) / 2f, offsetY + 24,
                0xFFFFFF, poseStack);
        String s4 = "\u00B0C";
        draw(font, s4,
                offsetX + 73 - font.width(s4) / 2f, offsetY + 24,
                0xF6DC47, poseStack);

        // GlStateManager._alphaFunc(GL11.GL_GREATER, 0.1F);
        if (!blend) {
            // RenderSystem.disableBlend();
            GL11.glDisable(GL11.GL_BLEND);
        }
    }

    private static void draw(Font font, String s, float f1, float f2, int i1,
                             PoseStack poseStack) {
        font.drawInBatch(s,
                f1, f2, i1, false, poseStack.last().pose(),
                Minecraft.getInstance().renderBuffers().bufferSource(),
                Font.DisplayMode.NORMAL, 0, 15728880);
    }

}
