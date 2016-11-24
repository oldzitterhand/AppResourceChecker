package com.oldzitterhand.app;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * TODO: Remove this. Implement property-config for file and folder in main window.
 * 
 * @author Patrick Metz
 */
public class PropTest {

	public static void main(String[] args) {
		String home = System.getProperty("user.home");
		System.err.println(home);
		
		Properties config = new Properties();
		File propsFile = new File(home, "test.properties");
        
		// read properties file
		try (InputStream inStream = Files.newInputStream(propsFile.toPath())) {
            config.load(inStream);
            for (Object key : config.keySet()) {
            	System.err.println("Key [" + key + "]" + " value [" + config.getProperty((String)key) + "]");
            }
        } catch (IOException e) {
			e.printStackTrace();
		}
        
		// write properties file
        try (OutputStream outStream = Files.newOutputStream(propsFile.toPath())) {
			final SimpleDateFormat timestampFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			config.setProperty("timestamp", timestampFormat.format(new Date()));
			config.store(outStream, "test");        	
        } catch (IOException e) {
			e.printStackTrace();
		}
        
	}
	
}
