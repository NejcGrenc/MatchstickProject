package grenc.masters.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import org.junit.AfterClass;
import org.junit.BeforeClass;


public class DatabaseTestConfiguraton
{
	protected static final String setupFile = "database_setup.sql";
	protected static final String cleanFile = "database_clean.sql";
	
	
	protected static PostgresConnector connector = new PostgresConnector();
	
	@BeforeClass
	public static void classSetup() throws SQLException, IOException
	{
		String script = readScript(setupFile);
		Connection connection = connector.open();
		connection.createStatement().execute(script);
		connector.close();	
	}
	
	@AfterClass
	public static void classCleanup() throws SQLException, IOException
	{
        String script = readScript(cleanFile);
		Connection connection = connector.open();
		connection.createStatement().execute(script);
		connector.close();	
	}
	
	private static String readScript(String fileName)
	{
		try (InputStream inputStream = DatabaseProperties.class.getClassLoader().getResourceAsStream(fileName)) 
		{
			return convertStreamToString(inputStream);
		} 
		catch (IOException ex) 
		{
			System.out.println("Problem occurs when reading file !");
			ex.printStackTrace();
			return "";
		}
	}
	
	private static String convertStreamToString(InputStream is) 
	{
		try (Scanner s = new Scanner(is))
		{
			s.useDelimiter("\\A");
		    return s.hasNext() ? s.next() : "";
		}
	}
}
