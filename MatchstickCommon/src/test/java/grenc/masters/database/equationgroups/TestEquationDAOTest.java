package grenc.masters.database.equationgroups;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import grenc.masters.database.test.DatabaseTestConfiguraton;


public class TestEquationDAOTest extends DatabaseTestConfiguraton
{
	private EquationDAO testEquationDAO;
	
	@Before
	public void setup() throws SQLException, IOException
	{
		testEquationDAO = new EquationDAO("test_equation");
		
		Connection connection = connector.open();
		connection.createStatement().execute("INSERT INTO test_equation (equation) VALUES ('1+2=3');");
		connector.close();	
	}

	@SuppressWarnings("deprecation")
	@Test
	public void insertAndFetchAll()
	{
		testEquationDAO.insert("4-5=9");
		
		List<EquationWrapper> taskData = testEquationDAO.findAllEquations();
		
		assertEquals(2, taskData.size());
		assertTrue(taskData.get(0).getEquation().equals("1+2=3") || taskData.get(1).getEquation().equals("1+2=3"));
		assertTrue(taskData.get(0).getEquation().equals("4-5=9") || taskData.get(1).getEquation().equals("4-5=9"));
	}
	
}
