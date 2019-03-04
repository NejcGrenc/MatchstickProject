package grenc.masters.database.dao;

import java.util.List;

import grenc.masters.database.builder.QueryBuilder;
import grenc.masters.database.builder.SelectBuilder;
import grenc.masters.database.entities.Subject;


public class SubjectDAO
{
	
	private static SubjectDAO instance = new SubjectDAO();
	
	private SubjectDAO() {}
	
	public static SubjectDAO getInstance()
	{
		return instance;
	}
	
	
	public synchronized Subject insertSubject(String name) 
	{
		QueryBuilder.newInsert().intoTable("subject")
					.setField("name", name)
					.setField("complete_data", false)
					.setField("original", false)
					.execute();

		return findLastSubjectByName(name);
	}
	
	public Subject findSubjectById(int subjectId)
	{
		return findSubjectWhere("id", subjectId);
	}
	
	public Subject findSubjectByIp(String ip)
	{
		return findSubjectWhere("ip", ip);
	}
	
	public List<Subject> findSubjectsByNameAndComplete(String name, boolean completeData)
	{
		List<Subject> subjects =
				  getFullSubject(QueryBuilder.newSelect(Subject::new))
				  .where("name", name)
				  .where("complete_data", completeData)
				  .orderByDesc("id", true)
				  .execute();
		
		return subjects;
	}
	
	protected Subject findLastSubjectByName(String name)
	{
		return findSubjectWhere("name", name);
	}
	
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
		  .getField("name", String.class, Subject::setName)
		  .getField("complete_data", Boolean.class, Subject::setCompleteData)
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
	
	public synchronized Subject updateSubjectOriginal(int subjectId, boolean original)
	{
		QueryBuilder.newUpdate().inTable("subject")
		.setField("original", original)
		.setCondition("id", subjectId)
		.execute();
		
		return findSubjectById(subjectId);
	}
	
}
