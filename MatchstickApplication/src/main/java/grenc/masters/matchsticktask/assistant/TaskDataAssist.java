package grenc.masters.matchsticktask.assistant;

import java.util.List;
import java.util.Optional;

import grenc.masters.database.dao.MatchstickTaskDataDAO;
import grenc.masters.database.dao.TaskSessionDAO;
import grenc.masters.database.entities.MatchstickTaskData;
import grenc.masters.database.entities.TaskSession;
import grenc.masters.matchsticktask.assistant.model.OrderedTaskData;
import grenc.masters.matchsticktask.type.MatchstickExperimentPhase;
import grenc.masters.matchsticktask.type.MatchstickTaskStatus;
import grenc.simpleton.annotation.Bean;
import grenc.simpleton.annotation.InsertBean;

@Bean
public class TaskDataAssist {

	@InsertBean
	private TaskSessionDAO taskSessionDAO;
	@InsertBean
	private MatchstickTaskDataDAO matchstickTaskDataDAO;

	
	public OrderedTaskData getOrderedTaskData(TaskSession taskSession)
	{
		List<MatchstickTaskData> taskDataList = matchstickTaskDataDAO.findAllTaskForSessionId(taskSession.getId());
		return new OrderedTaskData(taskDataList);
	}

	public boolean isFinished(TaskSession taskSession)
	{
		if (taskSession.isComplete())
			return true;
		
		if (shouldBeFinished(taskSession)) 
		{
			System.out.println("Should be finished, but it doesn't have 'complete' flag set!");
			return true;
		}
		return false;
	}
	
	public void finishItIfApplicable(TaskSession taskSession)
	{
		if (! taskSession.isComplete() && shouldBeFinished(taskSession))
			taskSessionDAO.updateComplete(taskSession.getId(), true);
	}
	
	private boolean shouldBeFinished(TaskSession taskSession)
	{
		return (newTaskNumber(taskSession) > totalNumberOfTasks(taskSession));
	}
	
	public int newTaskNumber(TaskSession taskSession)
	{
		Optional<MatchstickTaskData> lastTaskDataOptional = getOrderedTaskData(taskSession).lastTaskData();
		if (! lastTaskDataOptional.isPresent())
			return 1;
		
		MatchstickTaskData lastTaskData = lastTaskDataOptional.get();
		switch (lastTaskData.getStatus())
		{
			case stopped:
			case restarted:
				return lastTaskData.getNumber();

			case solved:
			default:
				return lastTaskData.getNumber() + 1;
		}
	}
	
	public MatchstickTaskStatus statusOfLastTask(TaskSession taskSession)
	{
		Optional<MatchstickTaskData> lastTaskDataOptional = getOrderedTaskData(taskSession).lastTaskData();
		if (! lastTaskDataOptional.isPresent())
			return null;
		return lastTaskDataOptional.get().getStatus();
	}
	public long timeOfLastTask(TaskSession taskSession)
	{
		Optional<MatchstickTaskData> lastTaskDataOptional = getOrderedTaskData(taskSession).lastTaskData();
		if (! lastTaskDataOptional.isPresent())
			return 0l;
		return lastTaskDataOptional.get().getTime();
	}
	
	public int getNoTasksForPhase(TaskSession taskSession, MatchstickExperimentPhase phase)
	{
		return taskSession.getMatchstickGroup().getNoTasksForPhase(phase);
	}
	public int getNoTasksUpToPhase(TaskSession taskSession, MatchstickExperimentPhase phase)
	{
		return taskSession.getMatchstickGroup().getNoTasksUpToPhase(phase);
	}
	public int totalNumberOfTasks(TaskSession taskSession)
	{
		 return taskSession.getMatchstickGroup().getNoTasks();
	}
}
