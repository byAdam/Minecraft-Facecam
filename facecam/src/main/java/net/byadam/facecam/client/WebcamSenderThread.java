package net.byadam.facecam.client;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;


import com.github.sarxos.webcam.Webcam;

import net.byadam.facecam.common.Facecam;
import net.byadam.facecam.common.FacecamMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;



public class WebcamSenderThread implements Runnable {
	
	Thread t;
	Webcam webcam = Webcam.getDefault();
	UUID uuid;
	public WebcamSenderThread instance;
	ClientPlayerEntity clientPlayer;
	FacecamClient facecamClient;
	boolean toggleWebcam = false;
	Integer packetNo = 0;
	
	public WebcamSenderThread(FacecamClient facecamClient)
	{		
		this.facecamClient = facecamClient;
		
		t = new Thread(this, "Thread");
		t.start();
	
	}
			

	public void run() 
	{
		while(true)
		{		
			try {
				if(toggleWebcam)
				{
					onToggleWebcam();
				}
				
				
				clientPlayer = Minecraft.getInstance().player;

				// Set UUID for sender
				if(uuid == null && clientPlayer != null)
				{
					uuid = Minecraft.getInstance().player.getUniqueID();
				}
				

				// Remove image when you leave server
				if(packetNo != 0 && clientPlayer == null)
				{
					sendWebcamOff();
				}
				
				// If transmission enabled and connected to server
				if(facecamClient.transmitWebcam && clientPlayer != null)
				{
					sendWebcam();
				}

				
				Thread.sleep(10);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				// Fail silently
			}
		}

	}
	
	
	
	
	public void toggleWebcam() { toggleWebcam(null); }
	
	// Get ready to run in thread
	public void toggleWebcam(Boolean toggleOn)
	{
		// If passed a toggle value
		if(toggleOn != null)
		{
			facecamClient.transmitWebcam = !toggleOn;
		}
		
		toggleWebcam = true;
	}
	
	// Run in thread
	void onToggleWebcam()
	{
		facecamClient.transmitWebcam ^= true;
		
		if(facecamClient.transmitWebcam)
		{
			try
			{
				webcam.open();	
			}
			catch(Exception e)
			{
				ITextComponent message = new StringTextComponent("§6§l[Facecam] §r§4Your webcam is being used by another program. Please close the program and try again.");
				Minecraft.getInstance().player.sendMessage(message, null);
				facecamClient.transmitWebcam = false;
			}
			
		}
		else
		{
			webcam.close();
			sendWebcamOff();
		}
		
		toggleWebcam = false;
	}
	
	public void sendWebcam() throws InterruptedException, IOException
	{
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		BufferedImage oldCamImage = webcam.getImage();
		Image scaledImage = oldCamImage.getScaledInstance(96, 96, Image.SCALE_SMOOTH);
		
		BufferedImage newCamImage = new BufferedImage(192, 96, BufferedImage.TYPE_INT_RGB);
	    Graphics2D graphics = newCamImage.createGraphics();
	    graphics.drawImage(scaledImage, 0, 0, null);
	    graphics.dispose();
	    
	    ImageIO.write(newCamImage, "JPEG", os);
	    
	    Facecam.packetHandler.sendToServer(new FacecamMessage(uuid, packetNo, os.toByteArray()));
	    packetNo += 1;
	}
	
	public void sendWebcamOff()
	{
	    packetNo = 0;
		Facecam.packetHandler.sendToServer(new FacecamMessage(uuid, -1, new byte[0]));
	}



}
