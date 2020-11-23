package net.byadam.facecam.common;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.client.ForgeHooksClient;
import net.byadam.facecam.client.CamLayer;
import net.byadam.facecam.client.WebcamsRP;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.IPackFinder;
import net.minecraft.resources.IPackNameDecorator;
import net.minecraft.resources.IResourcePack;
import net.minecraft.resources.PackCompatibility;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraft.resources.ResourcePackInfo.IFactory;
import net.minecraft.resources.ResourcePackList;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.resources.SimpleReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("facecam")
public class Facecam
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    private CamThread camThread;
 

    public Facecam() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event){}

    private void clientSetup(final FMLClientSetupEvent event) {
        // Add resource pack to pack list
        Minecraft.getInstance().getResourcePackList().addPackFinder(new IPackFinder() {
			@Override
			public void findPacks(Consumer<ResourcePackInfo> infoConsumer, IFactory infoFactory) {
				ResourcePackInfo resourcepackinfo = ResourcePackInfo.createResourcePack("webcams", true, () -> new WebcamsRP(), infoFactory, ResourcePackInfo.Priority.TOP, IPackNameDecorator.BUILTIN);
		        infoConsumer.accept(resourcepackinfo);
        }});
        
        // Reload all resource packs
        Minecraft.getInstance().getResourcePackList().reloadPacksFromFinders();
        
        // Start camera thread (TO BE CHANGED)
    }


    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {}
    
    public void addWebcamsToTextureManager()
    {
 	   SimpleReloadableResourceManager resourceManager =  ObfuscationReflectionHelper.getPrivateValue(TextureManager.class, Minecraft.getInstance().getTextureManager(), "resourceManager");
  	  
 	   // If facecamrp already in texture manager's resource manager
 	   if(resourceManager.getResourceNamespaces().contains("webcams")){return;}
 	   
 	   // If not, loop through all resource backs, find facecamrp, and add it
  	   for(ResourcePackInfo rp: Minecraft.getInstance().getResourcePackList().getEnabledPacks())
  	   {
  		   if(rp.getName() == "facecamrp")
  		   {
  			   resourceManager.addResourcePack(rp.getResourcePack());
  		   }
  	   }
    }
    
    // When player is rendered
	@SubscribeEvent
    public void onPlayerRender(RenderPlayerEvent.Pre event) 
    {
    	// TODO: Move this somewhere better 
    	addWebcamsToTextureManager();
    	
    	UUID playerUUID = event.getPlayer().getUniqueID();
    	
    	// For every 'skin' on the player, add the cam layer
		for (PlayerRenderer render : event.getRenderer().getRenderManager().getSkinMap().values()) 
		{
		    render.addLayer(new CamLayer(render, playerUUID));
		}
    }
}
