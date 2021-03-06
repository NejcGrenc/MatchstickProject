package grenc.masters.database.dao;

import java.util.List;

import grenc.masters.cache.annotation.Cached;
import grenc.masters.cache.annotation.ResetCache;
import grenc.masters.database.builder.QueryBuilder;
import grenc.masters.database.builder.SelectBuilder;
import grenc.masters.database.entities.MatchstickTaskData;
import grenc.simpleton.annotation.Bean;


@Bean
public class MatchstickTaskDataDAO
{
	
	private static MatchstickTaskDataDAO instance = new MatchstickTaskDataDAO();
	
	private MatchstickTaskDataDAO() {}
	
	public static MatchstickTaskDataDAO getInstance()
	{
		return instance;
	}
	
	@ResetCache
	public synchronized MatchstickTaskData insert(int taskSessionId, int number, int phaseNumber, String status, String originalEq, String solvedEq, long time, int moves, double transfer)
	{
		QueryBuilder.newInsert().intoTable("matchstick_task")
					.setField("task_session_id", taskSessionId)
					.setField("number", number)
					.setField("phase_number", phaseNumber)
					.setField("status", status)
					.setField("original_eq", originalEq)
					.setField("solved_eq", solvedEq)
					.setField("time", time)
					.setField("moves", moves)
					.setField("transfer", transfer)
					.execute();
		
		return findAllTaskForSessionId(taskSessionId).get(0);
	}
	
	@ResetCache
	public synchronized MatchstickTaskData insertInitial(int taskSessionId, int number, int phaseNumber)
	{
		QueryBuilder.newInsert().intoTable("matchstick_task")
					.setField("task_session_id", taskSessionId)
					.setField("number", number)
					.setField("phase_number", phaseNumber)
					.execute();
		
		return QueryBuilder.newSelect(MatchstickTaskData::new)
				.fromTable("matchstick_task")
				  .getField("id", Integer.class, MatchstickTaskData::setId)
				  .getField("task_session_id", Integer.class, MatchstickTaskData::setTaskSessionId)
				  .getField("number", Integer.class, MatchstickTaskData::setNumber)
				  .getField("phase_number", Integer.class, MatchstickTaskData::setPhaseNumber)
				  .where("task_session_id", taskSessionId)
				  .orderByDesc("id", true)
				  .execute()
				  .get(0);
	}
	
	@ResetCache
	public synchronized MatchstickTaskData update(int id, String status, String originalEq, String solvedEq, long time, long activityTime, int moves, double transfer)
	{
		QueryBuilder.newUpdate().inTable("matchstick_task")
					.setCondition("id", id)
					.setField("status", status)
					.setField("original_eq", originalEq)
					.setField("solved_eq", solvedEq)
					.setField("time", time)
					.setField("activity_time", activityTime)
					.setField("moves", moves)
					.setField("transfer", transfer)
					.execute();
		
		return findTaskById(id);
	}
	
	@Cached
	public MatchstickTaskData findTaskById(int id)
	{
		return fullQueryBuilder()
				  .where("id", id)
				  .execute().get(0);
	}
	
	@Cached
	public List<MatchstickTaskData> findAllTaskForSessionId(int taskSessionId)
	{
		return fullQueryBuilder()
				  .where("task_session_id", taskSessionId)
				  .orderByDesc("id", true)
				  .execute();
	}
	
	
	private SelectBuilder<MatchstickTaskData> fullQueryBuilder()
	{
		return QueryBuilder.newSelect(MatchstickTaskData::new)
				.fromTable("matchstick_task")
				  .getField("id", Integer.class, MatchstickTaskData::setId)
				  .getField("task_session_id", Integer.class, MatchstickTaskData::setTaskSessionId)
				  .getField("number", Integer.class, MatchstickTaskData::setNumber)
				  .getField("phase_number", Integer.class, MatchstickTaskData::setPhaseNumber)
				  .getField("status", String.class, MatchstickTaskData::setStatus)
				  .getField("original_eq", String.class, MatchstickTaskData::setOriginalEq)
				  .getField("solved_eq", String.class, MatchstickTaskData::setSolvedEq)
				  .getField("time", Long.class, MatchstickTaskData::setTime)
				  .getField("activity_time", Long.class, MatchstickTaskData::setTotalActivityTime)
				  .getField("moves", Integer.class, MatchstickTaskData::setMoves)
				  .getField("transfer", Float.class, MatchstickTaskData::setTransfer);
	}
}
