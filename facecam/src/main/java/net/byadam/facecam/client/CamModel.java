package net.byadam.facecam.client;

import org.apache.logging.log4j.LogManager;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3d;

public class CamModel extends AgeableModel {
	private final ModelRenderer model;
	
	public CamModel() {
		// Build Model Box
		this.model = new ModelRenderer(16, 8, 0,0);
		this.model.addBox(-4, -8, -4, 8, 8, 0);
		this.model.setRotationPoint(0, 4, 4);
	}

	// No 'head'
	protected Iterable<ModelRenderer> getHeadParts() {
		return ImmutableList.of();
	}

   // Body is the cam model
	protected Iterable<ModelRenderer> getBodyParts() {
		return ImmutableList.of(this.model);
	}

	@Override
	public void setRotationAngles(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		// Match model rotations to player head rotation
		
		this.model.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
		this.model.rotateAngleX = headPitch * ((float)Math.PI / 180F);
		

		if(entityIn.isSneaking())
		{
			this.model.setRotationPoint(0, 8.2f, 3.99f);
		}
		else if(entityIn.isActualySwimming())
		{
			AbstractClientPlayerEntity player = (AbstractClientPlayerEntity)entityIn;
			LogManager.getLogger().info(this.model.rotateAngleX);
			LogManager.getLogger().info(entityIn.rotationPitch);
			this.model.setRotationPoint(0, 4, 3.99f);

		}
		else
		{
			this.model.setRotationPoint(0, 4, 3.99f);
		}
	}
	
	// Taken from BipedModel
	float rotLerpRad(float angleIn, float maxAngleIn, float mulIn) {
	      float f = (mulIn - maxAngleIn) % ((float)Math.PI * 2F);
	      if (f < -(float)Math.PI) {
	         f += ((float)Math.PI * 2F);
	      }

	      if (f >= (float)Math.PI) {
	         f -= ((float)Math.PI * 2F);
	      }

	      return maxAngleIn + angleIn * f;
	   }
	
}
