package grenc.masters.database.dao;

import java.util.List;

import grenc.masters.cache.annotation.Cached;
import grenc.masters.cache.annotation.ResetCache;
import grenc.masters.database.builder.QueryBuilder;
import grenc.masters.database.builder.SelectBuilder;
import grenc.masters.database.entities.Subject;
import grenc.simpleton.annotation.Bean;


@Bean
public class SubjectDAO
{
	
	private static SubjectDAO instance = new SubjectDAO();
	
	private SubjectDAO() {}
	
	public static SubjectDAO getInstance()
	{
		return instance;
	}
	
	@ResetCache
	public synchronized Subject insertSubject(int sessionId) 
	{
		QueryBuilder.newInsert().intoTable("subject")
					.setField("session_id", sessionId)
					.setField("original", false)
					.execute();

		return findLastSubjectBySessionId(sessionId);
	}
	
	@Cached
	public Subject findSubjectById(int subjectId)
	{
		return findSubjectWhere("id", subjectId);
	}
	
	@Cached
	public Subject findSubjectByIp(String ip)
	{
		return findSubjectWhere("ip", ip);
	}
	
	@Cached
	protected Subject findLastSubjectBySessionId(int sessionId)
	{
		return findSubjectWhere("session_id", sessionId);
	}
	
	@Cached
	protected <T> Subject findSubjectWhere(String conditionName, T conditionValue)
	{
		List<Subject> subjects =
				  getFullSubject(QueryBuilder.newSelect(Subject::new))
				  .where(conditionName, conditionValue)
				  .orderByDesc("id", true)
				  .limit(1)
				  .execute();
		
		return (subjects.isEmpty()) ? null : subjects.get(0);
	}
	
	protected SelectBuilder<Subject> getFullSubject(SelectBuilder<Subject> builder)
	{
		builder.fromTable("subject")
		  .getField("id", Integer.class, Subject::setId)
		  .getField("session_id", Integer.class, Subject::setSessionId)
		  .getField("age", Integer.class, Subject::setAge)
		  .getField("sex", String.class, Subject::setSex)
		  .getField("language", String.class, Subject::setLanguage)
		  
		  .getField("ip", String.class, Subject::setIp)
		  .getField("address", String.class, Subject::setAddress)
		  .getField("operating_system", String.class, Subject::setOperatingSystem)
		  .getField("browser", String.class, Subject::setBrowser)
		  .getField("original", Boolean.class, Subject::setOriginal)
		  ;
		return builder;
	}
	
	@ResetCache
	public synchronized Subject updateSubject(int subjectId, Integer age, String sex, String lang)
	{
		QueryBuilder.newUpdate().inTable("subject")
		.setField("age", age)
		.setField("sex", sex)
		.setField("language", lang)
		.setCondition("id", subjectId)
		.execute();
		
		return findSubjectById(subjectId);
	}
	
	@ResetCache
	public synchronized Subject updateSubjectFetchedData(int subjectId, String ip, String address, String operatingSystem, String browser)
	{
		QueryBuilder.newUpdate().inTable("subject")
		.setField("ip", ip)
		.setField("address", address)
		.setField("operating_system", operatingSystem)
		.setField("browser", browser)
		.setCondition("id", subjectId)
		.execute();
		
		return findSubjectById(subjectId);
	}
	
	@ResetCache
	public synchronized Subject updateSubjectOriginal(int subjectId, boolean original)
	{
		QueryBuilder.newUpdate().inTable("subject")
		.setField("original", original)
		.setCondition("id", subjectId)
		.execute();
		
		return findSubjectById(subjectId);
	}
	
}
