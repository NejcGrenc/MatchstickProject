package grenc.masters.matchsticktask.assistant;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import grenc.masters.database.dao.TaskSessionDAO;
import grenc.masters.database.entities.Session;
import grenc.masters.database.entities.TaskSession;
import grenc.masters.matchsticktask.type.MatchstickGroup;
import grenc.masters.matchsticktask.type.TaskType;


public class TaskSessionAssist 
{
	private TaskSessionDAO taskSessionDAO;
	private GroupSelectAssist groupSelectAssist;
	
	private TaskType taskType;
	private Session session;
	private List<TaskSession> taskSessions;

	TaskSessionAssist(Session session, TaskType taskType, TaskSessionDAO taskSessionDAO, GroupSelectAssist groupSelectAssist)
	{
		this.taskSessionDAO = taskSessionDAO;
		this.groupSelectAssist = groupSelectAssist;
		
		this.session = session;
		this.taskType = taskType;
		
		// Currently we only support matchstick types
		this.taskType = TaskType.matchstick;
		
		init();
	}
	public TaskSessionAssist(Session session, TaskType taskType)
	{
		this (session, taskType, TaskSessionDAO.getInstance(), new GroupSelectAssist()); 
	}
	
	private void init()
	{
		this.taskSessions = taskSessionDAO.findAllTaskForSessionIdAndTaskType(session.getId(), this.taskType.name());
		// Order in reverse (last taskSessions first)
		this.taskSessions.stream().sorted(Comparator.comparingLong(TaskSession::getStartTime).reversed()).collect(Collectors.toList());	
	}
	
	public boolean hasAtLeastOneFinishedTaskSession()
	{
		return taskSessions.stream().anyMatch(taskSession -> taskSession.isComplete());
	}
	
	public TaskSession getTaskSessionToUse()
	{
		Optional<TaskSession> lastTaskSession = getLastTaskSession();
		TaskSession taskSessionToUse;
		
		if (! lastTaskSession.isPresent())
		{
			taskSessionToUse = createNewTaskSession();
			System.out.println("Creating first session: " + taskSessionToUse);
		}	
		else if (lastTaskSession.get().isComplete())
		{
			taskSessionToUse = createNewTaskSession();
			System.out.println("Creating new session: " + taskSessionToUse);
		}
		else {
			taskSessionToUse = lastTaskSession.get();
			System.out.println("Selecting unfinished session: " + taskSessionToUse);
		}
		
		return taskSessionToUse;
	}

	public Optional<TaskSession> getLastTaskSession()
	{
		return (taskSessions.isEmpty()) ? Optional.empty() : Optional.of(taskSessions.get(0));
	}
	
	public TaskSession createNewTaskSession()
	{
		MatchstickGroup newGroup = null;
		if (TaskType.matchstick.equals(taskType))
		{
			if (session.isTestTasksOnly())
				newGroup = MatchstickGroup.test;
			else
				newGroup = groupSelectAssist.selectNewGroup();
		}
			
		return taskSessionDAO.insert(session.getId(), taskType.name(), 
				new Date().getTime(), newGroup.toString(),
				false, null);		
	}

	
}
