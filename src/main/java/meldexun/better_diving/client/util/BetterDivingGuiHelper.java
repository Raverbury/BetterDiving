package meldexun.better_diving.client.util;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class BetterDivingGuiHelper {

    public static final double TWO_PI = 2.0D * Math.PI;

    private BetterDivingGuiHelper() {

    }

    public static int getAnchorX(int width, int anchor, int offsetX) {
        Window mainWindow = Minecraft.getInstance().getWindow();

        if (anchor == 1 || anchor == 4) {
            // top mid or bot mid
            return offsetX + mainWindow.getGuiScaledWidth() / 2 - width / 2;
        } else if (anchor == 2 || anchor == 3) {
            // top right or bot right
            return offsetX + mainWindow.getGuiScaledWidth() - width;
        }

        return offsetX;
    }

    public static int getAnchorY(int height, int anchor, int offsetY) {
        Window mainWindow = Minecraft.getInstance().getWindow();

        if (anchor == 3 || anchor == 4 || anchor == 5) {
            // bot right or bot mid or bot left
            return offsetY + mainWindow.getGuiScaledHeight() - height;
        }

        return offsetY;
    }

    public static void drawTexture(ResourceLocation texture, double x, double y,
                                   double u,
                                   double v, double width, double height, double texWidth, double texHeight) {

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.enableBlend();

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();

        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        Matrix4f matrix4f = new Matrix4f();
        buffer.vertex(matrix4f, (float) x, (float)(y + height), 0f).uv(
                (float) u,
                (float) (v + texHeight)).endVertex();

        buffer.vertex(matrix4f, (float) (x + width), (float)(y + height), 0f).uv(
                (float) (u + texWidth),
                (float) (v + texHeight)).endVertex();

        buffer.vertex(matrix4f, (float) (x + width), (float)(y), 0f).uv(
                (float) (u + texWidth),
                (float) (v)).endVertex();

        buffer.vertex(matrix4f, (float) x, (float)(y), 0f).uv(
                (float) u,
                (float) (v)).endVertex();

        tesselator.end();

        // GL11.glBegin(GL11.GL_QUADS);
        // GL11.glTexCoord2d(u, v + texHeight);
        // GL11.glVertex2d(x, y + height);
        //
        // GL11.glTexCoord2d(u + texWidth, v + texHeight);
        // GL11.glVertex2d(x + width, y + height);
        //
        // GL11.glTexCoord2d(u + texWidth, v);
        // GL11.glVertex2d(x + width, y);
        //
        // GL11.glTexCoord2d(u, v);
        // GL11.glVertex2d(x, y);
        // GL11.glEnd();
    }

}
