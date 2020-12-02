package net.byadam.facecam.common;

import net.byadam.facecam.client.FacecamClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("facecam")
public class Facecam
{
    // Directly reference a log4j logger.
    public static FacecamPacketHandler packetHandler = new FacecamPacketHandler();
    public static FacecamClient facecamClient;

    public Facecam() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
    	packetHandler.register();
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {}
    
    private void clientSetup(final FMLClientSetupEvent event) {
    	facecamClient = new FacecamClient();
    }

    @SubscribeEvent
    public void onPlayerLeave(PlayerLoggedOutEvent event)
    {
    	FacecamMessage facecamMessage = new FacecamMessage(event.getPlayer().getUniqueID(), -1, new byte[0]);
    	packetHandler.sendToServer(facecamMessage);
    }

}
