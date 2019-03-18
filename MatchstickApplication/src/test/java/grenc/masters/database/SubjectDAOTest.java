package grenc.masters.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import grenc.masters.database.dao.SubjectDAO;
import grenc.masters.database.entities.Subject;
import grenc.masters.database.test.DatabaseTestConfiguraton;


public class SubjectDAOTest extends DatabaseTestConfiguraton
{
	private SubjectDAO subjectDAO;
	
	@Before
	public void setup()
	{
		this.subjectDAO = SubjectDAO.getInstance();
	}
	
	@Test
	public void generateFullSubjectIncrementally()
	{
		Subject initialSubject = subjectDAO.insertSubject("Tom");
		assertNotNull(initialSubject);
		assertEquals("Tom", initialSubject.getName());
		
		subjectDAO.updateSubject(initialSubject.getId(), 24, "m", "si");
		
		Subject finalSubject = subjectDAO.findSubjectById(initialSubject.getId());
		
		assertNotNull(finalSubject);
		assertEquals(initialSubject.getId(), finalSubject.getId());
		assertEquals("Tom", finalSubject.getName());
		assertEquals(new Integer(24), finalSubject.getAge());
		assertEquals("m", finalSubject.getSex());
		assertEquals("si", finalSubject.getLanguage());
		//assertEquals(null, finalSubject.getPassword());
	}
	
}
