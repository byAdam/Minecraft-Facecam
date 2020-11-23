package com.byadam.facecam;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CamThread implements Runnable {	
	Thread t;
	public ByteArrayOutputStream oStream;
	public ByteArrayOutputStream newOStream;
	public static CamThread instance = null;
	
	
    private static final Logger LOGGER = LogManager.getLogger();
	
	CamThread() {
		t = new Thread(this, "Thread");
		t.start();
	}
   
   @Override
   public void run() {
      try 
      {
    	 loop();
      } 
      catch (MalformedURLException e) {}
      catch (IOException e) {}
      catch (InterruptedException e) {}
   }
   
   public void loop() throws MalformedURLException, IOException, InterruptedException
   {
  	 while(true)
  	 {
  		URL url = new URL("http://localhost:8000/output.png");
  	    URLConnection con = url.openConnection();
  	    con.setUseCaches(false);
  	    
  	    /*Files.createDirectories(Paths.get("assets/facecamrp/"));
  	    BufferedImage oStream = ImageIO.read(con.getInputStream());
  		// Save image to byte array
  	    if(oStream != null)
  	    {
  			ImageIO.write( oStream,"png", new File("assets/facecamrp/test_new.png"));
  	    }
  	    else
  	    {
  	    	LOGGER.info("NULL IMAGE");
 
  	    }*/
  	    
  	    newOStream = new ByteArrayOutputStream();
  	    BufferedImage img = ImageIO.read(con.getInputStream());
	    if(img != null)
	    {
			ImageIO.write(img ,"png", newOStream);
	    }
	    else
	    {
	    	LOGGER.info("NULL IMAGE");
   
    	}
  	    Thread.sleep(100);
  	 }
   }
   
   public static CamThread getInstance()
   {
	   if(instance == null)
	   {
		   instance = new CamThread();
	   }
	   
	   return instance;
   }
}
