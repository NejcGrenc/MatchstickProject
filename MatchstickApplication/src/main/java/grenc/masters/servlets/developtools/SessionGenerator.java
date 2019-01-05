package grenc.masters.servlets.developtools;

import java.util.UUID;

import grenc.masters.database.SessionDAO;
import grenc.masters.entities.Session;

public class SessionGenerator
{	
	private SessionDAO sessionDAO;

	SessionGenerator (SessionDAO sessionDAO)
	{
		this.sessionDAO = sessionDAO;
	}
	public SessionGenerator()
	{
		this (SessionDAO.getInstance());
	}
	
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
		System.out.println("Generated session: " + session);
		
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
