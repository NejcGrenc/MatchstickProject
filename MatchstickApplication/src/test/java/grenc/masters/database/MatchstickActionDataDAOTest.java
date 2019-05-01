package grenc.masters.database;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import grenc.masters.database.dao.MatchstickActionDataDAO;
import grenc.masters.database.test.DatabaseTestConfiguraton;

public class MatchstickActionDataDAOTest extends DatabaseTestConfiguraton
{
	private MatchstickActionDataDAO matchstickActionDataDAO;
	
	protected PostgresConnector connector = new PostgresConnector();
	
	@Before
	public void setup() throws SQLException, IOException
	{
		matchstickActionDataDAO = MatchstickActionDataDAO.getInstance();
		
		Connection connection = connector.open();
		connection.createStatement().execute("INSERT INTO matchstick_action "
				+ " (matchstick_task_id, start_eq, end_eq, start_matchstick_loc, end_matchstick_loc, start_time, end_time)"
				+ " VALUES (45, 'type', '5+7=3', '5+7=12', 'here', 'there', 10000, 20000),"
				+ "		   (55, 'type', '5+7=3', '5+7=12', 'here', 'there', 10000, 20000);");
		connector.close();	
	}
	
	
	@Test
	@Ignore
	public void insertAndFetchAll()
	{
		matchstickActionDataDAO.insert(45, "type", "5+7=3", "5+7=12", "here", "there", 10000L, 20000L);	
		assertEquals(2, matchstickActionDataDAO.findAllDataForMatchstickTaskId(45).size());
	}
	
}
