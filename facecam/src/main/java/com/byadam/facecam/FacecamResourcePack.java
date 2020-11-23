package com.byadam.facecam;

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

public  class FacecamResourcePack implements IResourcePack { 
    public FacecamResourcePack ( ) { 
        // constructor good basically empty 
    }
    
    private static final Logger LOGGER = LogManager.getLogger();
    
    @Override
    public InputStream getRootResourceStream(String fileName) throws IOException
    {
    	if(fileName == "pack.png")
    	{
        	return new FileInputStream(new File("pack.png"));
    	}
    	return null;

    }
    
    public boolean check(ByteArrayOutputStream image) throws IOException {
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
    	if(location.getPath() == "test")
    	{
    		
    		CamThread c = CamThread.getInstance();
    		
    		if(check(c.newOStream))
    		{
    			c.oStream = c.newOStream;
    		}
    		
    		return new ByteArrayInputStream(c.oStream.toByteArray());
    	}
    	else
    	{
    		return null;
    	}
    	
    }

    @Override
    public Collection<ResourceLocation> getAllResourceLocations(ResourcePackType type, String namespaceIn, String pathIn, int maxDepthIn, Predicate<String> filterIn)
    {
    	Set<ResourceLocation> c = new HashSet<ResourceLocation>();
    	c.add(new ResourceLocation("facecamrp:test"));
    	return c;
    }

    @Override
    public boolean resourceExists(ResourcePackType type, ResourceLocation location)
    {
    	if(location.getPath() == "test")
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }

    @Override
    public Set<String> getResourceNamespaces(ResourcePackType type)
    {
    	Set<String> s = new HashSet<String>();
    	s.add("facecamrp");
    	return s;
    }
    
    
    @Override
    public String getName()
    {
    	return "Facecam RP";
    }

    @Nullable
    public <T> T getMetadata(IMetadataSectionSerializer<T> deserializer) throws IOException {
       try (InputStream inputstream = new FileInputStream(new File("C:\\Users\\Adam\\Documents\\_Minecraft Projects\\Personal\\Minecraft-Facecam\\pack.mcmeta"))) {
          return ResourcePack.getResourceMetadata(deserializer, inputstream);
       } catch (FileNotFoundException | RuntimeException runtimeexception) {
          return (T)null;
       }
    }

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

}