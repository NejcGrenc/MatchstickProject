package grenc.masters.database;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import grenc.masters.database.dao.TaskSessionDAO;
import grenc.masters.database.test.DatabaseTestConfiguraton;


public class TaskSessionDAOTest extends DatabaseTestConfiguraton
{
	private TaskSessionDAO taskSessionDAO;
	
	@Before
	public void setup() throws SQLException, IOException
	{
		this.taskSessionDAO = TaskSessionDAO.getInstance();
		
		Connection connection = connector.open();
		connection.createStatement().execute("INSERT INTO task_session"
				+ "		   (session_id, task_type, start_time, matchstick_group, complete, notes)"
				+ " VALUES (45, 'type', 100000, 'test', true, 'I am the best'),"
				+ "		   (55, 'type2', 100000, null, false, 'I am the best');");
		connector.close();	
	}
	
	
	@Test
	@Ignore
	public void insertAndFetchAll()
	{
		taskSessionDAO.insert(45, "type", 100000L, "test", true, "I am the best");	
		assertEquals(2, taskSessionDAO.findAllTaskForSessionId(45).size());
	}
	
}
