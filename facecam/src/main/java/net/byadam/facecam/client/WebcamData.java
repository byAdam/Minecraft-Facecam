package net.byadam.facecam.client;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WebcamData {
	
	Map<UUID, byte[]> uuidImageMap = new HashMap<UUID, byte[]>();
	public static WebcamData instance;
	
	public WebcamData()
	{
		
	}
	
	public void onNewImage(UUID uuid, byte[] image)
	{
		uuidImageMap.put(uuid, image);
	}
	
	public static WebcamData getInstance()
	{
		if(instance == null)
		{
			instance = new WebcamData();
		}
		return instance;
	}
}
