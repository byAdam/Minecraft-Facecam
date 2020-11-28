package net.byadam.facecam.client;


import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;

import net.minecraft.client.renderer.entity.layers.CapeLayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resources.IPackFinder;
import net.minecraft.resources.IPackNameDecorator;
import net.minecraft.resources.IResourcePack;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraft.resources.SimpleReloadableResourceManager;
import net.minecraft.resources.FallbackResourceManager;
import net.minecraft.resources.ResourcePackInfo.IFactory;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class FacecamClient {
	
	public WebcamSenderThread webcamSenderThread;
	public WebcamData webcamData;
	
	public FacecamClient()
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

        webcamSenderThread = new WebcamSenderThread();
        webcamData = webcamData.getInstance();
        
        MinecraftForge.EVENT_BUS.register(this);
	}
	
    public void addWebcamsToTextureManager()
    {
 	   SimpleReloadableResourceManager resourceManager =  ObfuscationReflectionHelper.getPrivateValue(TextureManager.class, Minecraft.getInstance().getTextureManager(), "resourceManager");
 	   //SimpleReloadableResourceManager resourceManager =  ObfuscationReflectionHelper.getPrivateValue(TextureManager.class, Minecraft.getInstance().getTextureManager(), "field_110582_d");
    	
 	   // If webcams already in texture manager's resource manager
 	   if(resourceManager.getResourceNamespaces().contains("webcams")){return;}

 	   
 	   // If not, loop through all resource backs, find webcams, and add it
  	   for(ResourcePackInfo rp: Minecraft.getInstance().getResourcePackList().getEnabledPacks())
  	   {
  			LogManager.getLogger().info("FOUND PACK"+rp.getName());
  		   if(rp.getName() == "webcams")
  		   {

  			   resourceManager.addResourcePack(rp.getResourcePack());
  		   }
  	   }
    }
	
    public void addCamLayerToPlayer(RenderPlayerEvent.Pre event)
    {
      	UUID playerUUID = event.getPlayer().getUniqueID();
		for (PlayerRenderer render : event.getRenderer().getRenderManager().getSkinMap().values()) 
		{
			LogManager.getLogger("cam layer add "+event.getPlayer().getName().toString());
			CamLayer camLayer = new CamLayer(render);
		    render.addLayer(camLayer);
		}
    }
    
    
    // When player is rendered
	@SubscribeEvent
    public void onPlayerRender(RenderPlayerEvent.Pre event) 
    {
    	// TODO: Move this somewhere better 
    	addWebcamsToTextureManager();
    		
    	boolean hasCamLayer = false;
    	// Checks if they have cam layer
    	List<LayerRenderer> defaultLayers = ObfuscationReflectionHelper.getPrivateValue(LivingRenderer.class, event.getRenderer().getRenderManager().getSkinMap().get("default"), "layerRenderers");
    	//List<LayerRenderer> defaultLayers = ObfuscationReflectionHelper.getPrivateValue(LivingRenderer.class, event.getRenderer().getRenderManager().getSkinMap().get("default"), "field_177097_h");

      	for(LayerRenderer layer: defaultLayers)
      	{
		     if(layer.getClass() == CamLayer.class)
  		     {
  		    	 hasCamLayer = true;
  		    	 break;
  		     }
      	}
      	
    	
    	if(!hasCamLayer)
    	{
    		addCamLayerToPlayer(event);
    	}
    
    }
}
