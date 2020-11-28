package net.byadam.facecam.client;

import java.awt.event.KeyEvent;

import org.apache.logging.log4j.LogManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponentUtils;
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
		
		
		// Assign variables
		this.facecamClient = facecamClient;
		
		// Register Events
        MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
    void onClientTick(ClientTickEvent event)
    {
    	// Ignore if start tick
    	if(event.phase == Phase.START) { return; }
    	
        // check each enumerated key binding type for pressed and take appropriate action
        if(toggleWebcam.isPressed()) 
        {
        	onToggleWebcam();
        }
        
        if(toggleDisplay.isPressed()) 
        {
        	onToggleDisplay();
        }
    }
	
	void onToggleWebcam()
	{
		ITextComponent message;
		
		if(facecamClient.transmitWebcam)
		{
			message = new StringTextComponent("§6§l[Facecam] §r§4Webcam Disabled");
		}
		else
		{
			message = new StringTextComponent("§6§l[Facecam] §r§2Webcam Enabled");
		}
	
		

		
		Minecraft.getInstance().player.sendMessage(message, null);
		
		facecamClient.webcamSenderThread.toggleWebcam();
	}
	
	void onToggleDisplay()
	{
		ITextComponent message;
		
		if(facecamClient.displayWebcams)
		{
			message = new StringTextComponent("§6§l[Facecam] §r§4Display Disabled");
		}
		else
		{
			message = new StringTextComponent("§6§l[Facecam] §r§2Display Enabled");
		}
	
		
		Minecraft.getInstance().player.sendMessage(message, null);
		
		
    	facecamClient.displayWebcams ^= true;
	}
}
