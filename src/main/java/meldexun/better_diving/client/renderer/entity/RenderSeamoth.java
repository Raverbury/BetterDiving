package meldexun.better_diving.client.renderer.entity;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import meldexun.better_diving.BetterDiving;
import meldexun.better_diving.client.model.entity.ModelSeamoth;
import meldexun.better_diving.entity.EntitySeamoth;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderSeamoth extends EntityRenderer<EntitySeamoth> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(
            BetterDiving.MOD_ID, "textures/entity/seamoth.png");
    private final ModelSeamoth model;

    public RenderSeamoth(EntityRendererProvider.Context context) {
        super(context);
        model =
                new ModelSeamoth(
                        context.bakeLayer(ModelSeamoth.LAYER_LOCATION));
        this.shadowRadius = 0.5F;
    }

    @Override
    public void render(EntitySeamoth entityIn, float entityYaw,
                       float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferIn, int packedLightIn) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        poseStack.pushPose();

        if (mc.options.getCameraType() == CameraType.FIRST_PERSON) {
            poseStack.translate(0.0D, 0.8125D, 0.0D);
            this.setupRotation(entityIn, partialTicks, poseStack);
        } else {
            poseStack.translate(0.0D, 0.8125D, 0.0D);
            this.setupRotation(entityIn, partialTicks, poseStack);
        }

        poseStack.scale(-1.0F, -1.0F, 1.0F);
        model.renderToBuffer(poseStack,
                bufferIn.getBuffer(RenderType.entityTranslucentCull(TEXTURE)),
                packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F,
                1.0F);
        poseStack.popPose();

        super.render(entityIn, entityYaw, partialTicks, poseStack, bufferIn,
                packedLightIn);

        // otherwise entity will be invisible when player is outside of the water and entity is underwater
        ((MultiBufferSource.BufferSource) bufferIn).endBatch(
                RenderType.entityTranslucentCull(TEXTURE));
    }

    protected void setupRotation(EntitySeamoth entity, float partialTicks,
                                 PoseStack poseStack) {
        float yaw;
        float pitch;
        Minecraft mc = Minecraft.getInstance();
        if (entity.getControllingPassenger() == mc.player) {
            yaw = mc.player.getViewYRot(partialTicks);
            pitch = mc.player.getViewXRot(partialTicks);
        } else {
            yaw = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot());
            // yaw = 10f;
            pitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        }
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(-pitch));
    }

    @Override
    public ResourceLocation getTextureLocation(EntitySeamoth entity) {
        return TEXTURE;
    }

    protected float interpolateRotation(float prevRotation, float rotation, float partialTicks) {
        return prevRotation + Mth.wrapDegrees(
                rotation - prevRotation) * partialTicks;
    }

}
