package uk.org.woodcraft.bookings.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.struts2.util.ClassLoaderUtils;

public class Configuration {
	
	public static final String ENVIRONMENT = "environment";
	
	private static Configuration singleton;
	
	private Properties config;

	public Configuration() {
		config = loadConfig();
	}
	
	public Properties loadConfig()
	{
		Properties envProps = new Properties();
		try {
			InputStream stream = ClassLoaderUtils.getResourceAsStream("environment.properties", Configuration.class);
			//InputStream stream = Configuration.class.getResourceAsStream("environment.properties");
			if(stream == null)
				throw new FileNotFoundException("Unable to locate environment.properties");
			envProps.load(stream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		String environment = envProps.getProperty(ENVIRONMENT); 
		String propertiesFileName = "bookings."+environment+".properties";
		config = new Properties();
		try {
			InputStream stream =ClassLoaderUtils.getResourceAsStream(propertiesFileName, Configuration.class);
			if(stream == null)
				throw new FileNotFoundException("Unable to locate "+propertiesFileName);
			config.load(stream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		config.setProperty(ENVIRONMENT,environment);
		
		return config;
	}
	
	public String getProperty(String property)
	{
		return config.getProperty(property);
	}
	
	public boolean getBooleanProperty(String property)
	{
		String value = config.getProperty(property);
		return Boolean.valueOf(value);
	}
	
	public static Configuration get()
	{
		if(singleton == null)
		{
			synchronized (Configuration.class) {
				if (singleton == null)
				{
					singleton = new Configuration();
				}
			}
		}
		return singleton;
	}
}
