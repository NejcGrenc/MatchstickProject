package grenc.masters.database.dao;

import java.util.List;

import grenc.masters.database.builder.QueryBuilder;
import grenc.masters.database.builder.SelectBuilder;
import grenc.masters.database.entities.MatchstickTaskData;


public class MatchstickTaskDataDAO
{
	
	private static MatchstickTaskDataDAO instance = new MatchstickTaskDataDAO();
	
	private MatchstickTaskDataDAO() {}
	
	public static MatchstickTaskDataDAO getInstance()
	{
		return instance;
	}
	
	
	public synchronized MatchstickTaskData insert(int taskSessionId, int number, String status, String originalEq, String solvedEq, long time, int moves, double transfer)
	{
		QueryBuilder.newInsert().intoTable("matchstick_task")
					.setField("task_session_id", taskSessionId)
					.setField("number", number)
					.setField("status", status)
					.setField("original_eq", originalEq)
					.setField("solved_eq", solvedEq)
					.setField("time", time)
					.setField("moves", moves)
					.setField("transfer", transfer)
					.execute();
		
		return findAllTaskForSessionId(taskSessionId).get(0);
	}
	
	public synchronized MatchstickTaskData insertInitial(int taskSessionId, int number)
	{
		QueryBuilder.newInsert().intoTable("matchstick_task")
					.setField("task_session_id", taskSessionId)
					.setField("number", number)
					.execute();
		
		return QueryBuilder.newSelect(MatchstickTaskData::new)
				.fromTable("matchstick_task")
				  .getField("id", Integer.class, MatchstickTaskData::setId)
				  .getField("task_session_id", Integer.class, MatchstickTaskData::setTaskSessionId)
				  .getField("number", Integer.class, MatchstickTaskData::setNumber)
				  .where("task_session_id", taskSessionId)
				  .orderByDesc("id", true)
				  .execute()
				  .get(0);
	}
	
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
	
	public MatchstickTaskData findTaskById(int id)
	{
		return fullQueryBuilder()
				  .where("id", id)
				  .execute().get(0);
	}
	
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
				  .getField("status", String.class, MatchstickTaskData::setStatus)
				  .getField("original_eq", String.class, MatchstickTaskData::setOriginalEq)
				  .getField("solved_eq", String.class, MatchstickTaskData::setSolvedEq)
				  .getField("time", Long.class, MatchstickTaskData::setTime)
				  .getField("activity_time", Long.class, MatchstickTaskData::setTotalActivityTime)
				  .getField("moves", Integer.class, MatchstickTaskData::setMoves)
				  .getField("transfer", Float.class, MatchstickTaskData::setTransfer);
	}
}
