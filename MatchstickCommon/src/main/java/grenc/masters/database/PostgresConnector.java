package grenc.masters.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConnector
{
	private static String url = DatabaseProperties.getProperty("database.url");
    private static String database = DatabaseProperties.getProperty("database.database");
    private static String user = DatabaseProperties.getProperty("database.user");
    private static String password = DatabaseProperties.getProperty("database.password");

//    private static String user = "matchstickuser";
//  private static String password = "matchstickpass";
//  
//  private static String database = "matchstickdb";
//  private static String testDatabase = "matchstickdbtest";
    
//    private static String user = "matchstickuser";
//    private static String password = "matchstickpass";
//    
//    private static String database = "matchstickdb";
//    private static String testDatabase = "matchstickdbtest";
    
    private Connection connection = null;
    
    public Connection open() 
    {
    	try
		{
			Class.forName("org.postgresql.Driver");
		} 
    	catch (ClassNotFoundException e1)
		{
			e1.printStackTrace();
		}
    	
    	try
		{
//    		Context ctx = new InitialContext();
//	        if (ctx == null)
//	            throw new Exception("Boom - No Context");
//	        Context envCtx;
//			envCtx = (Context) ctx.lookup("java:comp/env");
//    		
//	        DataSource ds = (DataSource) envCtx.lookup("jdbc/matchstickdb");
//
//	        connection =  ds.getConnection();
//	        
			connection = DriverManager.getConnection(url + database, user, password);
		} 
		catch (Exception e)
		{
			System.out.println("Unable to open Postgres connection");
			e.printStackTrace();
		}
    	return connection;
    }
    
    public void close() {
    	try
		{
    		if (connection != null && !connection.isClosed())
    			connection.close();
    		connection = null;
		} 
    	catch (SQLException e)
		{
    		System.out.println("Postgres connection was unable to close");
			e.printStackTrace();
		}
    }
    
//    private String dbUrl()
//    {
//    	if (JunitTest.isJUnitTest())
//    	{
//    		return url + testDatabase;
//    	}
//    	return url + database;
//    }
    
}
