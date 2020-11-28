package net.byadam.facecam.client;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class WebcamData implements Runnable{
	
	public Map<UUID, ByteArrayOutputStream> uuidStream = new HashMap<UUID, ByteArrayOutputStream>();
	Map<UUID, byte[]> uuidRawImage = new ConcurrentHashMap<UUID, byte[]>();
	BufferedImage template;
	public static WebcamData instance;
	Thread t;
	
	public WebcamData()
	{
		t = new Thread(this, "Thread");
		t.start();
	}
	
	public void onNewImage(UUID uuid, byte[] image)
	{
		uuidRawImage.put(uuid, image);
	}
	
	public static WebcamData getInstance()
	{
		if(instance == null)
		{
			instance = new WebcamData();
		}
		return instance;
	}
	
	public void processRawImage(UUID uuid, byte[] rawImage) throws IOException
	{
		BufferedImage webcam = ImageIO.read(new ByteArrayInputStream(rawImage)); // 3ms
		Graphics2D graphics = template.createGraphics(); // 0ms
		graphics.drawImage(webcam, 14, 14, null); // 0ms
		ByteArrayOutputStream oStream = new ByteArrayOutputStream(); // 0ms
		ImageIO.write(template, "JPEG", oStream); // 5mms
		
		uuidStream.put(uuid, oStream);
	}

	@Override
	public void run() {
		// This line is horrible so TODO: Make it nicer
		try {
			template = ImageIO.read(Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation("webcams_i", "template.jpg")).getInputStream());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(true)
		{
			for (Map.Entry<UUID,byte[]> entry : uuidRawImage.entrySet())  
			{
				UUID key = entry.getKey();
				try {
					processRawImage(key, entry.getValue());
				} 
				catch (Exception e) {
				}
			}
			uuidRawImage.clear();
		}
	}
}
