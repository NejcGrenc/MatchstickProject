package grenc.masters.database.entities;

import grenc.masters.matchsticktask.type.MatchstickGroup;
import grenc.masters.matchsticktask.type.TaskType;

public class TaskSession
{
	private int id;
	private int sessionId;
	private TaskType taskType;
	private long startTime;
	private MatchstickGroup matchstickGroup;
	private boolean complete;
	private String notes;

	
	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getSessionId()
	{
		return sessionId;
	}

	public void setSessionId(int sessionId)
	{
		this.sessionId = sessionId;
	}

	public long getStartTime()
	{
		return startTime;
	}

	public void setStartTime(long startTime)
	{
		this.startTime = startTime;
	}
	
	public TaskType getTaskType()
	{
		return taskType;
	}

	public void setTaskType(String taskType)
	{
		if (taskType == null)
			return;
		this.taskType = TaskType.valueOf(taskType);
	}
	public void setTaskType(TaskType taskType)
	{
		if (taskType == null)
			return;
		this.taskType = taskType;
	}

	public MatchstickGroup getMatchstickGroup()
	{
		return matchstickGroup;
	}

	public void setMatchstickGroup(MatchstickGroup matchstickGroup)
	{
		if (matchstickGroup == null)
			return;
		this.matchstickGroup = matchstickGroup;
	}
	public void setMatchstickGroup(String matchstickGroupString)
	{
		if (matchstickGroupString == null)
			return;
		this.matchstickGroup = MatchstickGroup.valueOf(matchstickGroupString);
	}

	public boolean isComplete()
	{
		return complete;
	}

	public void setComplete(boolean complete)
	{
		this.complete = complete;
	}

	public String getNotes()
	{
		return notes;
	}

	public void setNotes(String notes)
	{
		this.notes = notes;
	}

	@Override
	public String toString()
	{
		return "TaskSession [id=" + id + ", sessionId=" + sessionId + ", taskType=" + taskType + ", startTime="
				+ startTime + ", matchstickGroup=" + matchstickGroup + ", complete=" + complete + ", notes=" + notes
				+ "]";
	}

}
