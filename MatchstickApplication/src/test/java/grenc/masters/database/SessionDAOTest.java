package grenc.masters.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import grenc.masters.database.dao.SessionDAO;
import grenc.masters.database.entities.Session;
import grenc.masters.database.test.DatabaseTestConfiguraton;

public class SessionDAOTest extends DatabaseTestConfiguraton
{
	private SessionDAO sessionDAO;
	
	@Before
	public void setup() throws SQLException, IOException
	{
		this.sessionDAO = SessionDAO.getInstance();
		
		Connection connection = connector.open();
		connection.createStatement().execute("insert into session (tag, risk, lang, subject_id, snoop_enabled)"
										   + " values ('123-123-123', 10, 'si', 1678, false);");
		connector.close();	
	}
	

	@Test
	public void selectFullSession()
	{
		Session session = sessionDAO.findSessionByTag("123-123-123");
		assertNotNull(session);
		assertEquals("123-123-123", session.getTag());
		assertEquals(10, session.getRisk());
		assertEquals("si", session.getLang());
		assertEquals(1678, session.getSubjectId());
	}
	
	@Test
	public void insertAndRetrieveSession()
	{
		Session session = sessionDAO.insertSession("456-456-456", 15, "en", 666);
		assertNotNull(session);
		assertNotNull(session.getId());
		assertEquals("456-456-456", session.getTag());
		assertEquals(15, session.getRisk());
		assertEquals("en", session.getLang());
		assertEquals(666, session.getSubjectId());
	}
	
	@Test
	public void updateSession()
	{
		Session originalSession = sessionDAO.findSessionByTag("123-123-123");
		sessionDAO.updateSessionRisk(originalSession.getId(), 20);
		sessionDAO.updateSessionLang(originalSession.getId(), "en");
		sessionDAO.updateSessionSubjectId(originalSession.getId(), 2000);
		
		Session updatedSession = sessionDAO.findSessionById(originalSession.getId());
		assertEquals(originalSession.getId(), updatedSession.getId());
		assertEquals("123-123-123", updatedSession.getTag());
		assertEquals(20, updatedSession.getRisk());
		assertEquals("en", updatedSession.getLang());
		assertEquals(2000, updatedSession.getSubjectId());
	}
	
}
