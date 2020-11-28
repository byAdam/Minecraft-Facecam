package net.byadam.facecam.client;

import java.awt.event.KeyEvent;

import org.apache.logging.log4j.LogManager;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyManager {
	
	public static KeyBinding toggleWebcam;
	public static KeyBinding toggleDisplay;
	public static FacecamClient facecamClient;

	public KeyManager(FacecamClient facecamClient)
	{
		// TODO: Change to translations
		toggleWebcam = new KeyBinding("Toggle Your Webcam", KeyEvent.VK_OPEN_BRACKET, "Facecam");
		toggleDisplay = new KeyBinding("Toggle All Webcams", KeyEvent.VK_CLOSE_BRACKET, "Facecam");
		
		ClientRegistry.registerKeyBinding(toggleWebcam);
		ClientRegistry.registerKeyBinding(toggleDisplay);
		
		
		// Assign facecamClient
		this.facecamClient = facecamClient;
		
		// Register Events
        MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
    public void onClientTick(ClientTickEvent event)
    {
    	// Ignore if start tick
    	if(event.phase == Phase.START) { return; }
    	
        // check each enumerated key binding type for pressed and take appropriate action
        if(toggleWebcam.isPressed()) 
        {
        	LogManager.getLogger().info("Toggle Webcam");
        	facecamClient.transmitWebcam ^= true;
        }
        
        if(toggleDisplay.isPressed()) 
        {
        	LogManager.getLogger().info("Toggle Display");
        	facecamClient.displayWebcam ^= true;
        }
    }
}
