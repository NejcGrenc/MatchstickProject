package grenc.masters.database.assist;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import grenc.masters.database.PostgresConnector;


public class DatabaseTestConfiguraton
{

	protected static PostgresConnector connector = new PostgresConnector();
	
	@BeforeClass
	public static void classSetup() throws SQLException, IOException
	{
		String scriptPath = "./WebContent/db-resources/database_setup.sql";
		
        String script = new String(Files.readAllBytes(Paths.get(scriptPath)));
		Connection connection = connector.open();
		connection.createStatement().execute(script);
		connector.close();	
	}
	
	@AfterClass
	public static void classCleanup() throws SQLException, IOException
	{
		String scriptPath = "./WebContent/db-resources/database_clean.sql";
		
        String script = new String(Files.readAllBytes(Paths.get(scriptPath)));
		Connection connection = connector.open();
		connection.createStatement().execute(script);
		connector.close();	
	}
	
}
