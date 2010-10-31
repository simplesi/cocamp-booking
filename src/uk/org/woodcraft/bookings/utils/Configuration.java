package uk.org.woodcraft.bookings.utils;

import java.io.IOException;
import java.util.Properties;

import org.apache.struts2.util.ClassLoaderUtils;

public class Configuration {
	
	public static final String ENVIRONMENT = "environment";
	
	private static final Configuration singleton = new Configuration();
	
	private Properties config;
	
	public Configuration() {
		Properties envProps = new Properties();
		try {
			envProps.load(ClassLoaderUtils.getResourceAsStream("environment.properties", Configuration.class));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		String environment = envProps.getProperty(ENVIRONMENT); 
		String propertiesFileName = "bookings."+environment+".properties";
		config = new Properties();
		try {
			config.load(ClassLoaderUtils.getResourceAsStream(propertiesFileName, Configuration.class));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		config.setProperty(ENVIRONMENT,environment);
	}
	
	public String getProperty(String property)
	{
		return config.getProperty(property);
	}
	
	public static Configuration get()
	{
		return singleton;
	}
}
