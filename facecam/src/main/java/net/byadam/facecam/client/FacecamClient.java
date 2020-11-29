package net.byadam.facecam.client;


import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resources.IPackFinder;
import net.minecraft.resources.IPackNameDecorator;
import net.minecraft.resources.IResourcePack;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraft.resources.SimpleReloadableResourceManager;
import net.minecraft.resources.FallbackResourceManager;
import net.minecraft.resources.ResourcePackInfo.IFactory;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class FacecamClient {
	
	public WebcamSenderThread webcamSenderThread;
	public WebcamData webcamData;
	public KeyManager keyManager;
	
	public boolean transmitWebcam = true;
	public boolean displayWebcams = true;

	
	public FacecamClient()
	{
        webcamSenderThread = new WebcamSenderThread(this);
        webcamSenderThread.toggleWebcam(true);
        
        
        webcamData = WebcamData.getInstance();
        
        addWebcamsToResourcePackList();
      	addWebcamsToTextureManager();
        addCamLayerToPlayer();
        
        keyManager = new KeyManager(this);
        
        // Register to events
        MinecraftForge.EVENT_BUS.register(this);
	}

	
	public void addWebcamsToResourcePackList()
	{
        // Add resource pack to pack list
        Minecraft.getInstance().getResourcePackList().addPackFinder(new IPackFinder() {
		@Override
		public void findPacks(Consumer<ResourcePackInfo> infoConsumer, IFactory infoFactory) {
			ResourcePackInfo resourcepackinfo = ResourcePackInfo.createResourcePack("webcams", true, () -> new WebcamsRP(), infoFactory, ResourcePackInfo.Priority.TOP, IPackNameDecorator.BUILTIN);
	        infoConsumer.accept(resourcepackinfo);
		}});
        
        // Reload all resource packs
        Minecraft.getInstance().getResourcePackList().reloadPacksFromFinders();
	}
	
    public void addWebcamsToTextureManager()
    {
 	   //SimpleReloadableResourceManager resourceManager =  ObfuscationReflectionHelper.getPrivateValue(TextureManager.class, Minecraft.getInstance().getTextureManager(), "resourceManager");
 	   SimpleReloadableResourceManager resourceManager =  ObfuscationReflectionHelper.getPrivateValue(TextureManager.class, Minecraft.getInstance().getTextureManager(), "field_110582_d");

 	   // If not, loop through all resource backs, find webcam's, and add it
  	   for(ResourcePackInfo rp: Minecraft.getInstance().getResourcePackList().getEnabledPacks())
  	   {
  		   if(rp.getName() == "webcams")
  		   {
  			   resourceManager.addResourcePack(rp.getResourcePack());
  		   }
  	   }
    }
	
    public void addCamLayerToPlayer()
    {
    	// Loops through each renderer in the skin maps
		for (PlayerRenderer render : Minecraft.getInstance().getRenderManager().getSkinMap().values()) 
		{
		    render.addLayer(new CamLayer(render, this));
		}
    }
}
