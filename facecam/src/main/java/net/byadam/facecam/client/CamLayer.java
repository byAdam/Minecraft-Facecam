package net.byadam.facecam.client;

import java.util.Map;
import java.util.UUID;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

@OnlyIn(Dist.CLIENT)
public class CamLayer<T extends LivingEntity, M extends EntityModel<T>> extends LayerRenderer<T, M> {
	
   private ResourceLocation TextureLocation;
   private final CamModel camModel = new CamModel();
   // Cached textures in texture manager, to be removed when pulling new texture
   private Map<String, Texture> mapTextureObjects =  ObfuscationReflectionHelper.getPrivateValue(TextureManager.class, Minecraft.getInstance().getTextureManager(), "mapTextureObjects");
   

   public CamLayer(IEntityRenderer<T, M> rendererIn, UUID uuid) {
      super(rendererIn);
      
      
      //TODO: Make this use UUID
      TextureLocation = new ResourceLocation("webcams", "webcam/"+uuid.toString());
   }

   // Render Model
   public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) 
   {
	   // Setting up Cam model
       matrixStackIn.push();
       matrixStackIn.translate(0.0D, -0.25D, -0.25D);
       this.getEntityModel().copyModelAttributesTo(this.camModel);
       this.camModel.setRotationAngles(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
       
       // Remove cached texture
	   mapTextureObjects.remove(TextureLocation);
	   
	   // Build new face with new texture
       IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntitySolid(TextureLocation));       
       	
       // Render cam and add to stack
       this.camModel.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
       matrixStackIn.pop();
	 }
  
}