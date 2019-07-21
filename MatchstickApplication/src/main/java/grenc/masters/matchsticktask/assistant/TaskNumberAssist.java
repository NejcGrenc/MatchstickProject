package grenc.masters.matchsticktask.assistant;

import java.util.Optional;

import grenc.masters.database.entities.MatchstickTaskData;
import grenc.masters.database.entities.TaskSession;
import grenc.masters.matchsticktask.type.MatchstickExperimentPhase;
import grenc.masters.matchsticktask.type.MatchstickGroup;
import grenc.simpleton.annotation.Bean;
import grenc.simpleton.annotation.InsertBean;

@Bean
public class TaskNumberAssist
{
	@InsertBean
	private TaskDataAssist taskDataAssist;

	public TaskNumberAssist() {}
	public TaskNumberAssist(TaskDataAssist taskDataAssist) 
	{
		this.taskDataAssist = taskDataAssist;
	}

	
	public Integer lastTaskLocalNumber(TaskSession taskSession)
	{
		Optional<MatchstickTaskData> lastTaskDataOptional = lastTaskData(taskSession);
		if (! lastTaskDataOptional.isPresent())
			return null;
		
		return lastTaskDataOptional.get().getPhaseNumber();
	}
	
	public int newTaskLocalNumber(TaskSession taskSession)
	{
		Optional<MatchstickTaskData> lastTaskDataOptional = lastTaskData(taskSession);
		if (! lastTaskDataOptional.isPresent())
			return 1;
		
		MatchstickTaskData lastTaskData = lastTaskDataOptional.get();
		switch (lastTaskData.getStatus())
		{
			case stopped:
			case restarted:
				return lastTaskData.getPhaseNumber();

			case solved:
			default:
				return getNextTaskLocalNumber(taskSession.getMatchstickGroup(), newTaskNumber(taskSession));
		}
	}
	
	// Should restart counter if learning phases are done
	private int getNextTaskLocalNumber(MatchstickGroup matchstickGroup, int nextTaskNumber)
	{
		int localTaskNumber = nextTaskNumber;
		if (localTaskNumber > matchstickGroup.getNoTasksForPhase(MatchstickExperimentPhase.LearningPhase_Showing))
			localTaskNumber = localTaskNumber - matchstickGroup.getNoTasksForPhase(MatchstickExperimentPhase.LearningPhase_Showing);
		if (localTaskNumber > matchstickGroup.getNoTasksForPhase(MatchstickExperimentPhase.LearningPhase_Solving))
			localTaskNumber = localTaskNumber - matchstickGroup.getNoTasksForPhase(MatchstickExperimentPhase.LearningPhase_Solving);
		return localTaskNumber;
	}
	
	public Integer lastTaskNumber(TaskSession taskSession)
	{
		Optional<MatchstickTaskData> lastTaskDataOptional = lastTaskData(taskSession);
		if (! lastTaskDataOptional.isPresent())
			return null;
		
		return lastTaskDataOptional.get().getNumber();
	}
	
	public int newTaskNumber(TaskSession taskSession)
	{
		Optional<MatchstickTaskData> lastTaskDataOptional = lastTaskData(taskSession);
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
	

	private Optional<MatchstickTaskData> lastTaskData(TaskSession taskSession)
	{
		return taskDataAssist.getOrderedTaskData(taskSession).lastTaskData();
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
