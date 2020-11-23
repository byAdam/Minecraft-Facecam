package net.byadam.facecam.client;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableSet;

import net.minecraft.resources.IResourcePack;
import net.minecraft.resources.ResourcePack;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.resources.data.IMetadataSectionSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.extensions.IForgeResourcePack;

public  class WebcamsRP implements IResourcePack { 
	private static Path basePath = Paths.get("assets/webcams");
	
    public WebcamsRP ( ) { 
    }
    
    @Override
    public InputStream getRootResourceStream(String fileName) throws IOException
    {
    	// Join Paths
    	Path path = basePath.resolve(fileName);
        if (Files.exists(path)) {
            return Files.newInputStream(path);
        }
    	return null;
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

    @Override
    public InputStream getResourceStream(ResourcePackType type, ResourceLocation location) throws IOException
    {
    	// If the request resource is a webcame
    	if(location.getPath().startsWith("webcam/"))
    	{
    		
    		//TODO: Reimplement this
    		
    		/*CamThread c = CamThread.getInstance();
    		
    		if(check(c.newOStream))
    		{
    			c.oStream = c.newOStream;
    		}
    		
    		return new ByteArrayInputStream(c.oStream.toByteArray());
    		
    		*/
    		
    		
    		
    		return null;
  
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

    
    @Override
    public Collection<ResourceLocation> getAllResourceLocations(ResourcePackType type, String namespaceIn, String pathIn, int maxDepthIn, Predicate<String> filterIn)
    {
    	// Currently left blank but TODO: Fix this so its acurate
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
          return (T)null;
       }
    }

	@Override
	public void close() {}

}