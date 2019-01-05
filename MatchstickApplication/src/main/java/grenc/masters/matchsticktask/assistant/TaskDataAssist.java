package grenc.masters.matchsticktask.assistant;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import grenc.masters.database.MatchstickTaskDataDAO;
import grenc.masters.database.TaskSessionDAO;
import grenc.masters.entities.MatchstickTaskData;
import grenc.masters.entities.TaskSession;


public class TaskDataAssist {

	private TaskSessionDAO taskSessionDAO;
	private MatchstickTaskDataDAO matchstickTaskDataDAO;
	
	private TaskSession taskSession;
	private List<MatchstickTaskData> taskDataList;

	TaskDataAssist(TaskSession taskSession, TaskSessionDAO taskSessionDAO, MatchstickTaskDataDAO matchstickTaskDataDAO)
	{
		this.taskSessionDAO = taskSessionDAO;
		this.matchstickTaskDataDAO = matchstickTaskDataDAO;
		
		this.taskSession = taskSession;
		
		init();
	}
	public TaskDataAssist(TaskSession taskSession)
	{
		this (taskSession, TaskSessionDAO.getInstance(), MatchstickTaskDataDAO.getInstance()); 
	}
	
	private void init()
	{
		this.taskDataList = matchstickTaskDataDAO.findAllTaskForSessionId(taskSession.getId());
		// Order in reverse (last taskData first)
		this.taskDataList.stream().sorted(Comparator.comparingLong(MatchstickTaskData::getId).reversed()).collect(Collectors.toList());	
	}
	
	public boolean isFinished()
	{
		if (taskSession.isComplete())
			return true;
		
		if (shouldBeFinished()) 
		{
			System.out.println("Should be finished, but it doesn't have 'complete' flag set!");
			return true;
		}
		return false;
	}
	
	public void finishItIfApplicable()
	{
		if (! taskSession.isComplete() && shouldBeFinished())
			taskSessionDAO.updateComplete(taskSession.getId(), true);
	}
	
	private boolean shouldBeFinished()
	{
		return (newTaskNumber() >= totalNumberOfTasks());
	}
	
	public int newTaskNumber()
	{
		Optional<MatchstickTaskData> lastTaskDataOptional = (taskDataList.isEmpty()) ? Optional.empty() : Optional.of(taskDataList.get(0));
		if (! lastTaskDataOptional.isPresent())
			return 1;
		
		MatchstickTaskData lastTaskData = lastTaskDataOptional.get();
		switch (lastTaskData.getStatus())
		{
			case paused:
			case restarted:
				return lastTaskData.getNumber();

			case solved:
			default:
				return lastTaskData.getNumber() + 1;
		}
	}
	
	public int totalNumberOfTasks()
	{
		 return taskSession.getMatchstickGroup().getNoTasks();
	}
}
