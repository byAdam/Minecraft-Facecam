package com.byadam.facecam;

import net.minecraft.client.renderer.entity.layers.*;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.model.ElytraModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CamLayer<T extends LivingEntity, M extends EntityModel<T>> extends LayerRenderer<T, M> {
   private static final ResourceLocation TEXTURE_ELYTRA = new ResourceLocation("textures/block/cobblestone.png");
   private final CamModel modelElytra = new CamModel();

   public CamLayer(IEntityRenderer<T, M> rendererIn) {
      super(rendererIn);
   }

   public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) 
   {
       ResourceLocation resourcelocation = TEXTURE_ELYTRA;
       
       matrixStackIn.push();
       matrixStackIn.translate(0.0D, -0.25D, -0.25D);
       this.getEntityModel().copyModelAttributesTo(this.modelElytra);
       this.modelElytra.setRotationAngles(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
       IVertexBuilder ivertexbuilder = ItemRenderer.getArmorVertexBuilder(bufferIn, this.getRenderType(), false, false);
       this.modelElytra.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
       matrixStackIn.pop();
	 }
   
   private RenderType getRenderType()
   {
	   return RenderType.getEntitySolid(TEXTURE_ELYTRA);
   }
 
}