package net.byadam.facecam.client;

import java.awt.Graphics2D;
import org.apache.commons.io.IOUtils;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableSet;

import net.byadam.facecam.common.Facecam;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResourcePack;
import net.minecraft.resources.ResourcePack;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.resources.data.IMetadataSectionSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.extensions.IForgeResourcePack;

public  class WebcamsRP implements IResourcePack { 
	// TODO: Replace with relative path
	private static Path basePath = Paths.get("/assets/facecam/");
	BufferedImage template;
	
	Map<UUID, InputStream> uuidStream = new HashMap<UUID, InputStream>();
	Map<UUID, Long> uuidUpdate = new HashMap<UUID, Long>();

	
    public WebcamsRP ( ) { 
		try {
			template = ImageIO.read(getRootResourceStream("template.jpg"));
		} catch (Exception e) {
		}
    }
    
    @Override
    public InputStream getRootResourceStream(String fileName) throws IOException
    {
    	return Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation("webcams_i"+ "", fileName)).getInputStream();
    }
    
    private boolean isImageValid(ByteArrayOutputStream image) throws IOException {
        try {
            BufferedImage bi = ImageIO.read(new ByteArrayInputStream(image.toByteArray()));
            bi.flush();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    String timeElapsed(long start)
    {
    	return String.valueOf(System.currentTimeMillis()-start);
    }
    
    void pasteWebcamOntoTemplate(byte Webcam[])
    {
    	
    }

    @Override
    public InputStream getResourceStream(ResourcePackType type, ResourceLocation location) throws IOException
    {
    	// If the request resource is a webcam
    	
    	if((location.getPath().endsWith(".mcmeta")))
    	{
    		return null;
    	}
    	
    	if(location.getPath().startsWith("webcam/"))
    	{
    		
			String stringUUID = location.getPath().split("/")[1];
  
    		UUID uuid = UUID.fromString(stringUUID);
    		
    		byte imageBytes[] = Facecam.facecamClient.webcamData.getInstance().uuidImageMap.get(uuid);

    		
    
    		long currentTime = System.currentTimeMillis();
    		
      		return getRootResourceStream("backup.png");
    		
    		/*if(imageBytes != null)
    		{	
    			BufferedImage webcam = ImageIO.read(new ByteArrayInputStream(imageBytes)); // 3ms
    			Graphics2D graphics = template.createGraphics(); // 0ms
    			graphics.drawImage(webcam, 28, 28, null); // 0ms
    			ByteArrayOutputStream oStream = new ByteArrayOutputStream(); // 0ms
    			ImageIO.write(template, "JPEG", oStream); // 5mms
    			ByteArrayInputStream iStream = new ByteArrayInputStream(oStream.toByteArray()); //0ms
    			
    			uuidUpdate.put(uuid, System.currentTimeMillis());
    			uuidStream.put(uuid, iStream);
    			return iStream;
    		}
    

    		return getRootResourceStream("backup.png");*/
  
    	}
    	else
    	{
    		// Join Paths
    		Path path = Paths.get(basePath.toString(), location.getPath());
            if (Files.exists(path)) {
                return Files.newInputStream(path);
            }
    	}
    	
		return null;
    	
    }
    
    public ByteArrayInputStream cloneInputStream(InputStream iStream) throws IOException
    {
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	IOUtils.copy(iStream, baos);
    	return new ByteArrayInputStream(baos.toByteArray());
    }
    
    
    @Override
    public Collection<ResourceLocation> getAllResourceLocations(ResourcePackType type, String namespaceIn, String pathIn, int maxDepthIn, Predicate<String> filterIn)
    {
    	// Currently left blank but TODO: Fix this so its accurate
    	return new HashSet<ResourceLocation>();
    }

    @Override
    public boolean resourceExists(ResourcePackType type, ResourceLocation location)
    {
    	// If the request resource is a webcame
    	if(location.getPath().startsWith("webcam/"))
    	{
    		return true;
    	}
    	else
    	{
    		// Join Paths
    		Path path = Paths.get(basePath.toString(), location.getPath());
            if (Files.exists(path)) {
                return true;
            }
    	}
    	
    	return false;
    }

    @Override
    public Set<String> getResourceNamespaces(ResourcePackType type)
    {
    	Set<String> s = new HashSet<String>();
    	s.add("webcams");
    	return s;
    }
    
    
    @Override
    public String getName()
    {
    	return "Webcams RP";
    }

    @Nullable
    public <T> T getMetadata(IMetadataSectionSerializer<T> deserializer) throws IOException {
       try (InputStream inputstream = getRootResourceStream("pack.mcmeta")) {
          return ResourcePack.getResourceMetadata(deserializer, inputstream);
       } catch (FileNotFoundException | RuntimeException runtimeexception) {
    	   throw(runtimeexception);
          //return (T)null;
       }
    }

	@Override
	public void close() {}

}