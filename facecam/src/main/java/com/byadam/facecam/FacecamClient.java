package com.byadam.facecam;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.client.ForgeHooksClient;


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
public class FacecamClient
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    private CamThread camThread;
    
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
    	    new ResourceLocation("facecam", "webcam"),
    	    () -> PROTOCOL_VERSION,
    	    PROTOCOL_VERSION::equals,
    	    PROTOCOL_VERSION::equals
    	);

    public FacecamClient() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
 
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        Minecraft.getInstance().getResourcePackList().addPackFinder(new IPackFinder() {
			@Override
			public void findPacks(Consumer<ResourcePackInfo> infoConsumer, IFactory infoFactory) {
				ResourcePackInfo resourcepackinfo = ResourcePackInfo.createResourcePack("facecamrp", true, () -> new FacecamResourcePack(), infoFactory, ResourcePackInfo.Priority.TOP, IPackNameDecorator.BUILTIN);
		        infoConsumer.accept(resourcepackinfo);
        }});
        
        Minecraft.getInstance().getResourcePackList().reloadPacksFromFinders();
        
        
        camThread = CamThread.getInstance();
 	   
    }


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
    
    public void addFacecamrp()
    {
 	   SimpleReloadableResourceManager r =  ObfuscationReflectionHelper.getPrivateValue(TextureManager.class, Minecraft.getInstance().getTextureManager(), "resourceManager");
  	  
 	   if(r.getResourceNamespaces().contains("facecamrp"))
 	   {
 		   return;
 	   }
 	   
  	   for(ResourcePackInfo x: Minecraft.getInstance().getResourcePackList().getEnabledPacks())
  	   {
  		   if(x.getName() == "facecamrp")
  		   {
  			   r.addResourcePack(x.getResourcePack());
  		   }
  	   }
    }

    @SubscribeEvent
    public void onPlayerRender(RenderPlayerEvent.Pre event) {
    	addFacecamrp();
    	
	  for (PlayerRenderer render : event.getRenderer().getRenderManager().getSkinMap().values()) {
	    	render.addLayer(new CamLayer(render));
	    	
		  }

    }
}
