package grenc.masters.database.dao;

import java.util.List;

import grenc.masters.database.builder.QueryBuilder;
import grenc.masters.database.entities.Session;

public class SessionDAO
{
	
	private static SessionDAO instance = new SessionDAO();
	
	private SessionDAO() {}
	
	public static SessionDAO getInstance()
	{
		return instance;
	}
	
	
	public synchronized Session insertSession(String tag, int risk, String lang, Integer subjectId) 
	{
		QueryBuilder.newInsert().intoTable("session")
					.setField("tag", tag)
					.setField("risk", risk)
					.setField("lang", lang)
					.setField("subject_id", subjectId)
					.setField("test_tasks_only", false)
					.setField("snoop_enabled", false)
					.execute();

		return findSessionByTag(tag);
	}
	
	
	public Session findSessionById(int sessionId)
	{
		return findSessionWhere("id", sessionId);
	}
	
	public Session findSessionByTag(String sessionTag)
	{
		if (sessionTag == null)
			throw new RuntimeException("SessionTag cannot be null");
		return findSessionWhere("tag", sessionTag);
	}
	
	protected <T> Session findSessionWhere(String conditionField, T conditionValue)
	{
		List<Session> sessions = QueryBuilder.newSelect(Session::new)
				  .fromTable("session")
				  .getField("id", Integer.class, Session::setId)
				  .getField("tag", String.class, Session::setTag)
				  .getField("risk", Integer.class, Session::setRisk)
				  .getField("lang", String.class, Session::setLang)
				  .getField("subject_id", Integer.class, Session::setSubjectId)
				  .getField("test_tasks_only", Boolean.class, Session::setTestTasksOnly)
				  .getField("snoop_enabled", Boolean.class, Session::setSnoopEnabled)
				  .where(conditionField, conditionValue)
				  .orderByDesc("id", true)
				  .limit(1)
				  .execute();
		
		return (sessions.isEmpty()) ? null : sessions.get(0);
	}
	
	
	public synchronized Session updateSessionRisk(int sessionId, int newRisk)
	{
		updateSessionField(sessionId, "risk", newRisk);
		return findSessionById(sessionId);
	}
	
	public synchronized Session updateSessionLang(int sessionId, String newLang)
	{
		updateSessionField(sessionId, "lang", newLang);
		return findSessionById(sessionId);
	}
	
	public synchronized Session updateSessionSubjectId(int sessionId, int newSubjectId)
	{
		updateSessionField(sessionId, "subject_id", newSubjectId);
		return findSessionById(sessionId);
	}
	
	public Session updateSessionTestTasksOnly(int sessionId, boolean testTasksOnly)
	{
		updateSessionField(sessionId, "test_tasks_only", testTasksOnly);
		return findSessionById(sessionId);
	}
	
	public Session updateSessionSnoopEnabled(int sessionId, boolean snoopEnabled)
	{
		updateSessionField(sessionId, "snoop_enabled", snoopEnabled);
		return findSessionById(sessionId);
	}
	
	protected synchronized <T> void updateSessionField(int sessionId, String fieldName, T fieldValue)
	{
		QueryBuilder.newUpdate().inTable("session")
					.setField(fieldName, fieldValue)
					.setCondition("id", sessionId)
					.execute();
	}
	
}
