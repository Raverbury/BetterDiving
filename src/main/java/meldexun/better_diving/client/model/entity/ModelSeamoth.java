package meldexun.better_diving.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import meldexun.better_diving.BetterDiving;
import meldexun.better_diving.entity.EntitySeamoth;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelSeamoth extends EntityModel<EntitySeamoth> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(new ResourceLocation(BetterDiving.MOD_ID,
                    "modelseamoth"), "main");
    private final ModelPart bone;
    private final ModelPart left_wing;
    private final ModelPart right_wing;
    private final ModelPart antena;
    private final ModelPart window;
    private final ModelPart propellar;
    private final ModelPart back;
    private final ModelPart light;

    public ModelSeamoth(ModelPart root) {
        this.bone = root.getChild("bone");
        this.left_wing = root.getChild("left_wing");
        this.right_wing = root.getChild("right_wing");
        this.antena = root.getChild("antena");
        this.window = root.getChild("window");
        this.propellar = root.getChild("propellar");
        this.back = root.getChild("back");
        this.light = root.getChild("light");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-12.0F, 12.0F, -12.0F, 24.0F, 1.0F, 24.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(0, 25)
                        .addBox(12.0F, 2.0F, -13.0F, 1.0F, 10.0F, 26.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(0, 61)
                        .addBox(-13.0F, 2.0F, -13.0F, 1.0F, 10.0F, 26.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(96, 0)
                        .addBox(-12.0F, -13.0F, 12.0F, 24.0F, 25.0F, 1.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(54, 25)
                        .addBox(-13.0F, -13.0F, 8.0F, 1.0F, 15.0F, 5.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(54, 45)
                        .addBox(12.0F, -13.0F, 8.0F, 1.0F, 15.0F, 5.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(66, 26)
                        .addBox(-8.0F, 11.0F, -13.0F, 16.0F, 1.0F, 1.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(66, 28)
                        .addBox(-12.0F, 2.0F, -13.0F, 4.0F, 10.0F, 1.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(66, 39)
                        .addBox(8.0F, 2.0F, -13.0F, 4.0F, 10.0F, 1.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(146, 0)
                        .addBox(-4.0F, -13.0F, -7.0F, 8.0F, 1.0F, 1.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(146, 2)
                        .addBox(-5.0F, -13.0F, -6.0F, 10.0F, 1.0F, 2.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(146, 5)
                        .addBox(-6.0F, -13.0F, -4.0F, 12.0F, 1.0F, 3.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(146, 9)
                        .addBox(-7.0F, -13.0F, -1.0F, 14.0F, 1.0F, 3.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(146, 13)
                        .addBox(-8.0F, -13.0F, 2.0F, 16.0F, 1.0F, 3.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(146, 17)
                        .addBox(-9.0F, -13.0F, 5.0F, 18.0F, 1.0F, 2.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(146, 20)
                        .addBox(-10.0F, -13.0F, 7.0F, 20.0F, 1.0F, 1.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(146, 22)
                        .addBox(-12.0F, -13.0F, 8.0F, 24.0F, 1.0F, 4.0F,
                                new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_wing = partdefinition.addOrReplaceChild("left_wing",
                CubeListBuilder.create().texOffs(416, 128)
                        .addBox(9.0F, 10.0F, -29.0F, 4.0F, 1.0F, 16.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(352, 152)
                        .addBox(9.0F, 2.0F, -30.0F, 4.0F, 8.0F, 1.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(416, 162)
                        .addBox(13.0F, 10.0F, -28.0F, 3.0F, 1.0F, 44.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(352, 161)
                        .addBox(13.0F, 2.0F, -29.0F, 3.0F, 8.0F, 1.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(352, 170)
                        .addBox(16.0F, 2.0F, -28.0F, 2.0F, 3.0F, 1.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(416, 207)
                        .addBox(16.0F, 10.0F, -27.0F, 2.0F, 1.0F, 42.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(416, 400)
                        .addBox(22.0F, 10.0F, -19.0F, 1.0F, 1.0F, 26.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(416, 291)
                        .addBox(19.0F, 10.0F, -25.0F, 1.0F, 1.0F, 38.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(416, 250)
                        .addBox(18.0F, 10.0F, -26.0F, 1.0F, 1.0F, 40.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(416, 330)
                        .addBox(20.0F, 10.0F, -24.0F, 1.0F, 1.0F, 36.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(416, 367)
                        .addBox(21.0F, 10.0F, -22.0F, 1.0F, 1.0F, 32.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(352, 216)
                        .addBox(22.0F, 2.0F, -22.0F, 1.0F, 8.0F, 3.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(352, 206)
                        .addBox(21.0F, 2.0F, -24.0F, 1.0F, 3.0F, 2.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(352, 197)
                        .addBox(20.0F, 2.0F, -25.0F, 1.0F, 8.0F, 1.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(352, 188)
                        .addBox(19.0F, 2.0F, -26.0F, 1.0F, 8.0F, 1.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(352, 179)
                        .addBox(18.0F, 2.0F, -27.0F, 1.0F, 8.0F, 1.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(352, 227)
                        .addBox(23.0F, 2.0F, -19.0F, 1.0F, 8.0F, 26.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(352, 327)
                        .addBox(12.0F, 2.0F, 17.0F, 1.0F, 8.0F, 1.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(352, 309)
                        .addBox(16.0F, 2.0F, 15.0F, 2.0F, 3.0F, 1.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(352, 318)
                        .addBox(13.0F, 2.0F, 16.0F, 3.0F, 8.0F, 1.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(352, 300)
                        .addBox(18.0F, 2.0F, 14.0F, 1.0F, 8.0F, 1.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(352, 282)
                        .addBox(20.0F, 2.0F, 12.0F, 1.0F, 8.0F, 1.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(352, 291)
                        .addBox(19.0F, 2.0F, 13.0F, 1.0F, 8.0F, 1.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(352, 272)
                        .addBox(21.0F, 2.0F, 10.0F, 1.0F, 8.0F, 2.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(352, 261)
                        .addBox(22.0F, 2.0F, 7.0F, 1.0F, 8.0F, 3.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(416, 427)
                        .addBox(12.0F, 10.0F, 13.0F, 1.0F, 1.0F, 4.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(256, 128)
                        .addBox(9.0F, 1.0F, -29.0F, 4.0F, 1.0F, 16.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(256, 400)
                        .addBox(22.0F, 1.0F, -19.0F, 1.0F, 1.0F, 26.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(256, 367)
                        .addBox(21.0F, 1.0F, -22.0F, 1.0F, 1.0F, 32.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(256, 330)
                        .addBox(20.0F, 1.0F, -24.0F, 1.0F, 1.0F, 36.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(256, 291)
                        .addBox(19.0F, 1.0F, -25.0F, 1.0F, 1.0F, 38.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(256, 250)
                        .addBox(18.0F, 1.0F, -26.0F, 1.0F, 1.0F, 40.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(256, 207)
                        .addBox(16.0F, 1.0F, -27.0F, 2.0F, 1.0F, 42.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(256, 162)
                        .addBox(13.0F, 1.0F, -28.0F, 3.0F, 1.0F, 44.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(256, 427)
                        .addBox(12.0F, 1.0F, 13.0F, 1.0F, 1.0F, 4.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(352, 128)
                        .addBox(8.0F, 2.0F, -29.0F, 1.0F, 8.0F, 16.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(352, 175)
                        .addBox(16.0F, 7.0F, -28.0F, 2.0F, 3.0F, 1.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(352, 211)
                        .addBox(21.0F, 7.0F, -24.0F, 1.0F, 3.0F, 2.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(352, 314)
                        .addBox(16.0F, 7.0F, 15.0F, 2.0F, 3.0F, 1.0F,
                                new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_wing = partdefinition.addOrReplaceChild(
                "right_wing", CubeListBuilder.create().texOffs(96, 291)
                        .addBox(-20.0F, 2.0F, 13.0F, 1.0F, 8.0F, 1.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(160, 128)
                        .addBox(-13.0F, 10.0F, -29.0F, 4.0F, 1.0F, 16.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(96, 152)
                        .addBox(-13.0F, 2.0F, -30.0F, 4.0F, 8.0F, 1.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(160, 162)
                        .addBox(-16.0F, 10.0F, -28.0F, 3.0F, 1.0F, 44.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(96, 161)
                        .addBox(-16.0F, 2.0F, -29.0F, 3.0F, 8.0F, 1.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(96, 170)
                        .addBox(-18.0F, 2.0F, -28.0F, 2.0F, 3.0F, 1.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(160, 207)
                        .addBox(-18.0F, 10.0F, -27.0F, 2.0F, 1.0F, 42.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(160, 400)
                        .addBox(-23.0F, 10.0F, -19.0F, 1.0F, 1.0F, 26.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(160, 291)
                        .addBox(-20.0F, 10.0F, -25.0F, 1.0F, 1.0F, 38.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(160, 250)
                        .addBox(-19.0F, 10.0F, -26.0F, 1.0F, 1.0F, 40.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(160, 330)
                        .addBox(-21.0F, 10.0F, -24.0F, 1.0F, 1.0F, 36.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(160, 367)
                        .addBox(-22.0F, 10.0F, -22.0F, 1.0F, 1.0F, 32.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(96, 216)
                        .addBox(-23.0F, 2.0F, -22.0F, 1.0F, 8.0F, 3.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(96, 206)
                        .addBox(-22.0F, 2.0F, -24.0F, 1.0F, 3.0F, 2.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(96, 197)
                        .addBox(-21.0F, 2.0F, -25.0F, 1.0F, 8.0F, 1.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(96, 188)
                        .addBox(-20.0F, 2.0F, -26.0F, 1.0F, 8.0F, 1.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(96, 179)
                        .addBox(-19.0F, 2.0F, -27.0F, 1.0F, 8.0F, 1.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(96, 227)
                        .addBox(-24.0F, 2.0F, -19.0F, 1.0F, 8.0F, 26.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(96, 327)
                        .addBox(-13.0F, 2.0F, 17.0F, 1.0F, 8.0F, 1.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(96, 309)
                        .addBox(-18.0F, 2.0F, 15.0F, 2.0F, 3.0F, 1.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(96, 318)
                        .addBox(-16.0F, 2.0F, 16.0F, 3.0F, 8.0F, 1.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(96, 300)
                        .addBox(-19.0F, 2.0F, 14.0F, 1.0F, 8.0F, 1.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(96, 282)
                        .addBox(-21.0F, 2.0F, 12.0F, 1.0F, 8.0F, 1.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(96, 272)
                        .addBox(-22.0F, 2.0F, 10.0F, 1.0F, 8.0F, 2.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(96, 261)
                        .addBox(-23.0F, 2.0F, 7.0F, 1.0F, 8.0F, 3.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(160, 427)
                        .addBox(-13.0F, 10.0F, 13.0F, 1.0F, 1.0F, 4.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(0, 128)
                        .addBox(-13.0F, 1.0F, -29.0F, 4.0F, 1.0F, 16.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(0, 400)
                        .addBox(-23.0F, 1.0F, -19.0F, 1.0F, 1.0F, 26.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(0, 291)
                        .addBox(-20.0F, 1.0F, -25.0F, 1.0F, 1.0F, 38.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(0, 367)
                        .addBox(-22.0F, 1.0F, -22.0F, 1.0F, 1.0F, 32.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(0, 330)
                        .addBox(-21.0F, 1.0F, -24.0F, 1.0F, 1.0F, 36.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(0, 250)
                        .addBox(-19.0F, 1.0F, -26.0F, 1.0F, 1.0F, 40.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(0, 207)
                        .addBox(-18.0F, 1.0F, -27.0F, 2.0F, 1.0F, 42.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(0, 162)
                        .addBox(-16.0F, 1.0F, -28.0F, 3.0F, 1.0F, 44.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(0, 427)
                        .addBox(-13.0F, 1.0F, 13.0F, 1.0F, 1.0F, 4.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(96, 128)
                        .addBox(-9.0F, 2.0F, -29.0F, 1.0F, 8.0F, 16.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(96, 314)
                        .addBox(-18.0F, 7.0F, 15.0F, 2.0F, 3.0F, 1.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(96, 211)
                        .addBox(-22.0F, 7.0F, -24.0F, 1.0F, 3.0F, 2.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(96, 175)
                        .addBox(-18.0F, 7.0F, -28.0F, 2.0F, 3.0F, 1.0F,
                                new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition antena = partdefinition.addOrReplaceChild("antena",
                CubeListBuilder.create().texOffs(480, 0)
                        .addBox(-1.0F, -8.0F, 0.0F, 2.0F, 8.0F, 2.0F,
                                new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, -13.0F, 10.0F));

        PartDefinition window = partdefinition.addOrReplaceChild("window",
                CubeListBuilder.create().texOffs(0, 448)
                        .addBox(-13.0F, -12.9F, -13.0F, 26.0F, 0.0F, 21.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(94, 448)
                        .addBox(-13.0F, -13.0F, -12.9F, 26.0F, 24.0F, 0.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(146, 448)
                        .addBox(-12.9F, -13.0F, -13.0F, 0.0F, 15.0F, 21.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(188, 448)
                        .addBox(12.9F, -13.0F, -13.0F, 0.0F, 15.0F, 21.0F,
                                new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition propellar = partdefinition.addOrReplaceChild("propellar",
                CubeListBuilder.create().texOffs(448, 0)
                        .addBox(-0.5F, -3.5F, -0.5F, 1.0F, 7.0F, 1.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(448, 8)
                        .addBox(-3.5F, -0.5F, -0.5F, 7.0F, 1.0F, 1.0F,
                                new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 28.5F));

        PartDefinition back = partdefinition.addOrReplaceChild("back",
                CubeListBuilder.create().texOffs(256, 0)
                        .addBox(-12.0F, -12.0F, 13.0F, 24.0F, 24.0F, 6.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(256, 30)
                        .addBox(-11.0F, -11.0F, 19.0F, 22.0F, 22.0F, 3.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(256, 55)
                        .addBox(-10.0F, -10.0F, 22.0F, 20.0F, 20.0F, 2.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(256, 77)
                        .addBox(-9.0F, -9.0F, 24.0F, 18.0F, 18.0F, 2.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(316, 0)
                        .addBox(-8.0F, -8.0F, 26.0F, 16.0F, 4.0F, 3.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(316, 7)
                        .addBox(-8.0F, 4.0F, 26.0F, 16.0F, 4.0F, 3.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(316, 14)
                        .addBox(4.0F, -4.0F, 26.0F, 4.0F, 8.0F, 3.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(316, 25)
                        .addBox(-8.0F, -4.0F, 26.0F, 4.0F, 8.0F, 3.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(316, 36)
                        .addBox(-4.0F, -4.0F, 26.0F, 8.0F, 8.0F, 1.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(316, 45)
                        .addBox(-0.5F, -0.5F, 27.0F, 1.0F, 1.0F, 3.0F,
                                new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition light = partdefinition.addOrReplaceChild("light",
                CubeListBuilder.create().texOffs(0, 480)
                        .addBox(16.0F, 5.0F, -28.0F, 2.0F, 2.0F, 1.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(0, 480)
                        .addBox(21.0F, 5.0F, -24.0F, 1.0F, 2.0F, 2.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(0, 480)
                        .addBox(-18.0F, 5.0F, -28.0F, 2.0F, 2.0F, 1.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(0, 480)
                        .addBox(-22.0F, 5.0F, -24.0F, 1.0F, 2.0F, 2.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(0, 480)
                        .addBox(16.0F, 5.0F, 15.0F, 2.0F, 2.0F, 1.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(0, 480)
                        .addBox(-18.0F, 5.0F, 15.0F, 2.0F, 2.0F, 1.0F,
                                new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 512, 512);
    }

    @Override
    public void setupAnim(EntitySeamoth entitySeamoth, float v, float v1, float v2, float v3, float v4) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red,
                green, blue, alpha);
        left_wing.render(poseStack, vertexConsumer, packedLight, packedOverlay,
                red, green, blue, alpha);
        right_wing.render(poseStack, vertexConsumer, packedLight, packedOverlay,
                red, green, blue, alpha);
        antena.render(poseStack, vertexConsumer, packedLight, packedOverlay,
                red, green, blue, alpha);
        window.render(poseStack, vertexConsumer, packedLight, packedOverlay,
                red, green, blue, alpha);
        propellar.render(poseStack, vertexConsumer, packedLight, packedOverlay,
                red, green, blue, alpha);
        back.render(poseStack, vertexConsumer, packedLight, packedOverlay, red,
                green, blue, alpha);
        light.render(poseStack, vertexConsumer, packedLight, packedOverlay, red,
                green, blue, alpha);
    }
}