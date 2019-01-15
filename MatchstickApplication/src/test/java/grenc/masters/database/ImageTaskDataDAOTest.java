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
import grenc.masters.entities.ImageTaskData;


public class ImageTaskDataDAOTest extends DatabaseTestConfiguraton
{
	private ImageTaskDataDAO imageTaskDataDAO;
	
	@Before
	public void setup() throws SQLException, IOException
	{
		imageTaskDataDAO = ImageTaskDataDAO.getInstance();
		
		Connection connection = connector.open();
		connection.createStatement().execute("INSERT INTO images_task "
				+ "        (task_session_id, number,image_id, time, correct)"
				+ " VALUES (45, 3, 'url//', 123456, true),"
				+ "		   (55, 13, 'url//', 123456, true);");
		connector.close();	
	}

	@Test
	public void insertAndFetchAll()
	{
		imageTaskDataDAO.insert(45, 4, "//url2", 3333L, false);
		
		List<ImageTaskData> taskData = imageTaskDataDAO.findAllTaskForSessionId(45);
		
		assertEquals(2, taskData.size());
		
		assertNotNull(taskData.get(0));
		assertEquals(45, taskData.get(0).getTaskSessionId());
		assertEquals(4, taskData.get(0).getNumber());
		assertEquals("//url2", taskData.get(0).getImageId());
		assertEquals(3333L, taskData.get(0).getTime());
		assertEquals(false, taskData.get(0).isCorrect());

		assertNotNull(taskData.get(1));
		assertEquals(45, taskData.get(1).getTaskSessionId());
		assertEquals(3, taskData.get(1).getNumber());
		assertEquals("url//", taskData.get(1).getImageId());
		assertEquals(123456L, taskData.get(1).getTime());
		assertEquals(true, taskData.get(1).isCorrect());
		
	}
	
}
