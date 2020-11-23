package com.byadam.facecam;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class CamModel extends AgeableModel {
	private final ModelRenderer model;
	
	public CamModel() {
		this.model = new ModelRenderer(18, 9, 0,0);
		this.model.addBox(-4, -8, -4, 8, 8, 1);
		this.model.setRotationPoint(0, 4, 4);
	}
	
   protected Iterable<ModelRenderer> getHeadParts() {
      return ImmutableList.of();
   }

   protected Iterable<ModelRenderer> getBodyParts() {
      return ImmutableList.of(this.model);
   }

@Override
public void setRotationAngles(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks,
		float netHeadYaw, float headPitch) {
    this.model.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
    this.model.rotateAngleX = headPitch * ((float)Math.PI / 180F);
}
}
