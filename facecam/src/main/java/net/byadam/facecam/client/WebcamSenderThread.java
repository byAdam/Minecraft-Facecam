package net.byadam.facecam.client;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;

import com.github.sarxos.webcam.Webcam;

import net.byadam.facecam.common.Facecam;
import net.byadam.facecam.common.FacecamMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;



public class WebcamSenderThread implements Runnable {
	
	Thread t;
	Webcam webcam = Webcam.getDefault();
	UUID uuid;
	public WebcamSenderThread instance;
	public boolean transmitWebcam = true;
	ClientPlayerEntity clientPlayer;
	
	public WebcamSenderThread()
	{		
		t = new Thread(this, "Thread");
		t.start();
	}
			

	public void run() 
	{
		webcam.open();
		while(true)
		{		
			try {
				clientPlayer = Minecraft.getInstance().player;

				// Set UUID for sender
				if(uuid == null && clientPlayer != null)
				{
					uuid = Minecraft.getInstance().player.getUniqueID();
				}
				
				// If transmission enabled and connected to server
				if(Facecam.facecamClient.transmitWebcam && clientPlayer != null)
				{
					sendWebcam();
				}

				
				Thread.sleep(20);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				// Fail silently
			}
		}

	}
	
	public void sendWebcam() throws InterruptedException, IOException
	{
		
		if(uuid != null)
		{
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			
			BufferedImage oldCamImage = webcam.getImage();
			Image scaledImage = oldCamImage.getScaledInstance(128, 128, Image.SCALE_SMOOTH);
			
			BufferedImage newCamImage = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
		    Graphics2D graphics = newCamImage.createGraphics();
		    graphics.drawImage(scaledImage, 0, 0, null);
		    graphics.dispose();
		    
		    ImageIO.write(newCamImage, "JPEG", os);
	
		    Facecam.packetHandler.sendToServer(new FacecamMessage(uuid, os.toByteArray()));
		}
	}
}
