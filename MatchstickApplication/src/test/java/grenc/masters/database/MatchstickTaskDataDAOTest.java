package grenc.masters.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import grenc.masters.database.test.DatabaseTestConfiguraton;
import grenc.masters.entities.MatchstickTaskData;

public class MatchstickTaskDataDAOTest extends DatabaseTestConfiguraton
{
	private MatchstickTaskDataDAO matchstickTaskDataDAO;
	
	@Before
	public void setup() throws SQLException, IOException
	{
		this.matchstickTaskDataDAO = MatchstickTaskDataDAO.getInstance();
		
		Connection connection = connector.open();
		connection.createStatement().execute("INSERT INTO matchstick_task "
				+ "        (task_session_id, number, original_eq, solved_eq, time, moves, transfer)"
				+ " VALUES (45, 3, 'type', '2+2=7', '5+2=7', 1234, 2, 2.66),"
				+ "		   (55, 4, 'type', '2+2=7', '5+2=7', 1234, 2, 2.66);");
		connector.close();	
	}
	

	@Test
	public void insertAndFetchAll()
	{
		matchstickTaskDataDAO.insert(45, 5, "type", "2+2=7", "5+2=7", 1234, 2, 2.66);
		
		List<MatchstickTaskData> taskData = matchstickTaskDataDAO.findAllTaskForSessionId(45);
		
		assertEquals(2, taskData.size());
		
		assertNotNull(taskData.get(0));
		assertEquals(45, taskData.get(0).getTaskSessionId());
		assertEquals(5, taskData.get(0).getNumber());
		assertEquals("2+2=7", taskData.get(0).getOriginalEq());
		assertEquals("5+2=7", taskData.get(0).getSolvedEq());
		assertEquals(new Long(1234), taskData.get(0).getTime());
		assertEquals(2, taskData.get(0).getMoves());
		assertEquals(new Float(2.66), new Float(taskData.get(0).getTransfer()));

		assertNotNull(taskData.get(1));
		assertEquals(45, taskData.get(1).getTaskSessionId());
		assertEquals(3, taskData.get(1).getNumber());
		assertEquals("2+2=7", taskData.get(1).getOriginalEq());
		assertEquals("5+2=7", taskData.get(1).getSolvedEq());
		assertEquals(new Long(1234), taskData.get(1).getTime());
		assertEquals(2, taskData.get(1).getMoves());
		assertEquals(new Float(2.66), new Float(taskData.get(1).getTransfer()));
	}
	
}
