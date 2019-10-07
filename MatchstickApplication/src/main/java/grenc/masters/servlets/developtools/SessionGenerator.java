package grenc.masters.servlets.developtools;

import java.util.UUID;

import grenc.masters.database.dao.SessionDAO;
import grenc.masters.database.entities.Session;
import grenc.masters.utils.Logger;
import grenc.simpleton.annotation.Bean;
import grenc.simpleton.annotation.InsertBean;

@Bean
public class SessionGenerator
{	
	@InsertBean
	private SessionDAO sessionDAO;
	
	private Logger logger = new Logger(SessionGenerator.class.getSimpleName());

	public Session generateSession()
	{
		String randomString = null;
		boolean valid = false;
		while (!valid)
		{
			randomString = generateRandomString();
			if (sessionNotExists(randomString))
			{
				valid = true;
			}
		}
		
		Session session = sessionDAO.insertSession(randomString, 0, null, 0);
		logger.log(session, "Generated session: " + session);
		
		return session;
	}
	
	public String generateRandomString() 
	{
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	private boolean sessionNotExists(String sessionTag)
	{
		return (sessionDAO.findSessionByTag(sessionTag) == null);
	}
	
}
