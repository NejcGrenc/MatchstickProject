package grenc.masters.database;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseProperties 
{
	protected static final String filePath = "database.properties";
	
	protected static Properties prop = new Properties();

	static 
	{
		try (InputStream inputStream = DatabaseProperties.class.getClassLoader().getResourceAsStream(filePath)) {

			// Loading the properties.
			prop.load(inputStream);

//			// Getting properties
//			String url = prop.getProperty("database.url");
//			String login = prop.getProperty("database.login");
//			String password = prop.getProperty("database.password");
//			
//			System.out.println("URL = " + url);
//			System.out.println("login = " + login);
//			System.out.println("password = " + password);
		} catch (IOException ex) {
			System.out.println("Problem occurs when reading file !");
			ex.printStackTrace();
		} 
	}
	
	public static String getProperty(String name)
	{
		return prop.getProperty(name);
	}
}
