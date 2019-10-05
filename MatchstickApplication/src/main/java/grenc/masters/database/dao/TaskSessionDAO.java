package grenc.masters.database.dao;

import java.util.List;

import grenc.masters.cache.annotation.Cached;
import grenc.masters.cache.annotation.ResetCache;
import grenc.masters.database.builder.QueryBuilder;
import grenc.masters.database.builder.SelectBuilder;
import grenc.masters.database.entities.TaskSession;
import grenc.simpleton.annotation.Bean;


@Bean
public class TaskSessionDAO
{
	
	private static TaskSessionDAO instance = new TaskSessionDAO();
	
	private TaskSessionDAO() {}
	
	public static TaskSessionDAO getInstance()
	{
		return instance;
	}
	
	@ResetCache
	public synchronized TaskSession insert(int sessionId, String taskType, Long startTime, String matchstickGroup, boolean complete, String notes)
	{
		QueryBuilder.newInsert().intoTable("task_session")
					.setField("session_id", sessionId)
					.setField("task_type", taskType)
					.setField("start_time", startTime)
					.setField("matchstick_group", matchstickGroup)
					.setField("complete", complete)
					.setField("notes", notes)
					.execute();
		
		return findAllTaskForSessionId(sessionId).get(0);
	}
	
	@Cached
	public TaskSession findTaskForTaskSessionId(int taskSessionId)
	{
		List<TaskSession> taskSessions = buildFullEntry(QueryBuilder.newSelect(TaskSession::new))
					  .where("id", taskSessionId)
					  .orderByDesc("id", true)
					  .execute();
		
		return (taskSessions == null) ? null : taskSessions.get(0);
	}
	
	@Cached
	public List<TaskSession> findAllTaskForSessionId(int sessionId)
	{
		return buildFullEntry(QueryBuilder.newSelect(TaskSession::new))
					  .where("session_id", sessionId)
					  .orderByDesc("id", true)
					  .execute();
	}
	
	@Cached
	public List<TaskSession> findAllTaskForType(String taskType)
	{
		return buildFullEntry(QueryBuilder.newSelect(TaskSession::new))
				  	  .where("task_type", taskType)
					  .execute();
	}
	
	@Cached
	public List<TaskSession> findAllTaskForSessionIdAndTaskType(int sessionId, String taskType)
	{
		return buildFullEntry(QueryBuilder.newSelect(TaskSession::new))
					  .where("session_id", sessionId)
					  .where("task_type", taskType)
					  .orderByDesc("id", true)
					  .execute();
	}
	
	@Cached
	public List<TaskSession> findAllTaskForSessionIdAndTaskTypeAndComplete(int sessionId, String taskType, boolean complete)
	{
		return buildFullEntry(QueryBuilder.newSelect(TaskSession::new))
					  .where("session_id", sessionId)
					  .where("task_type", taskType)
					  .where("complete", complete)
					  .orderByDesc("id", true)
					  .execute();
	}
	
	protected SelectBuilder<TaskSession> buildFullEntry(SelectBuilder<TaskSession> builder)
	{
		builder.fromTable("task_session")
		  .getField("id", Integer.class, TaskSession::setId)
		  .getField("session_id", Integer.class, TaskSession::setSessionId)
		  .getField("task_type", String.class, TaskSession::setTaskType)
		  .getField("start_time", Long.class, TaskSession::setStartTime)
		  .getField("matchstick_group", String.class, TaskSession::setMatchstickGroup)
		  .getField("complete", Boolean.class, TaskSession::setComplete)
		  .getField("notes", String.class, TaskSession::setNotes);
		return builder;
	}
	
	@ResetCache
	public synchronized TaskSession updateComplete(int id, boolean complete)
	{
		QueryBuilder.newUpdate().inTable("task_session")
					.setCondition("id", id)
					.setField("complete", complete)
					.execute();
		return findTaskForTaskSessionId(id);
	}
	
	@ResetCache
	public synchronized TaskSession updateNotes(int id, String notes)
	{
		QueryBuilder.newUpdate().inTable("task_session")
					.setCondition("id", id)
					.setField("notes", notes)
					.execute();
		return findTaskForTaskSessionId(id);
	}
}
