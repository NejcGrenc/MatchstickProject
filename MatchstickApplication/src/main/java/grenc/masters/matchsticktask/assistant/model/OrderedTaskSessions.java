package grenc.masters.matchsticktask.assistant.model;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import grenc.masters.database.entities.TaskSession;

public class OrderedTaskSessions
{
	private List<TaskSession> taskSessionList;
	
	public OrderedTaskSessions(List<TaskSession> taskSessionList) 
	{
		this.taskSessionList = order(taskSessionList);
	}
	
	/* Order in reverse (last taskData first) */
	protected List<TaskSession> order(List<TaskSession> taskSessionList)
	{
		return taskSessionList.stream()
				.sorted(Comparator.comparingLong(TaskSession::getId).reversed())
				.collect(Collectors.toList());	
	}
	
	public Optional<TaskSession> lastTaskSession()
	{
		return (taskSessionList.isEmpty()) ? Optional.empty() : Optional.of(taskSessionList.get(0));
	}
	
	public boolean hasAtLeastOneFinishedTaskSession()
	{
		return taskSessionList.stream().anyMatch(taskSession -> taskSession.isComplete());
	}
}
