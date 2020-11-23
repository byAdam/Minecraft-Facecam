package com.byadam.facecam;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.DownloadingTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.resources.FallbackResourceManager;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.resources.SimpleReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

@OnlyIn(Dist.CLIENT)
public class CamLayer<T extends LivingEntity, M extends EntityModel<T>> extends LayerRenderer<T, M> {
   private static final ResourceLocation TEXTURE_ELYTRA = new ResourceLocation("facecamrp", "test");
   private final CamModel modelElytra = new CamModel();

   
   private static final Logger LOGGER = LogManager.getLogger();
   
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
       
       IVertexBuilder ivertexbuilder = bufferIn.getBuffer(this.getRenderType(resourcelocation));       
       
       this.modelElytra.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
       matrixStackIn.pop();
	 }
   
   private RenderType getRenderType(ResourceLocation resourcelocation)
   {
	   ((Map) ObfuscationReflectionHelper.getPrivateValue(TextureManager.class, Minecraft.getInstance().getTextureManager(), "mapTextureObjects")).remove(resourcelocation);

	   RenderType rt = RenderType.getEntitySolid(resourcelocation);
	   return rt;
   }
  
}