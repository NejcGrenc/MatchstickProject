package grenc.masters.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseProperties 
{
	protected static final String filePath = "database.properties";
	
	protected static final String localProperties = "/application-environment.properties";
	
	protected static Properties prop = new Properties();

	static 
	{
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader == null)
		{
			System.out.println("Class loader is null");
			classLoader = Thread.currentThread().getContextClassLoader();
			if (classLoader == null)
			{
				System.out.println("Class loader (by thread) is null");
			}
		}
				
		try (InputStream inputStream = new DatabaseProperties().getClass().getClassLoader().getResourceAsStream(filePath)) 
		{
			// Loading the properties.
			prop.load(inputStream);
		} 
		catch (IOException ex) 
		{
			System.out.println("Problem occurs when reading file !");
			ex.printStackTrace();
		} 
		
		try (InputStream inputStream = new FileInputStream(localProperties))
		{
			// Loading the properties.
			prop.load(inputStream);
		} 
		catch (IOException ex) 
		{
			System.out.println("Local properties cannot be found on path: " + localProperties);
		}
		
		 printOutAllProperties();
	}
	
	public static String getProperty(String name)
	{
		return prop.getProperty(name);
	}
	
	public static void printOutAllProperties()
	{
		System.out.println("PROPERTIES");
		for (Object pro : prop.keySet())
			System.out.println("Property: '" + pro + "' is '" + prop.getProperty((String)pro) + "'");
	}
}
