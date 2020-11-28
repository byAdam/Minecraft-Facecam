package net.byadam.facecam.client;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

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
			this.model.setRotationPoint(0, 8.2f, 4);
		}
		else
		{
			this.model.setRotationPoint(0, 4, 4);
		}
	}
}
