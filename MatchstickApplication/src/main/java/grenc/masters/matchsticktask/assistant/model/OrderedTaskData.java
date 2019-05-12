package grenc.masters.matchsticktask.assistant.model;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import grenc.masters.database.entities.MatchstickTaskData;

public class OrderedTaskData
{
	private List<MatchstickTaskData> taskDataList;
	
	public OrderedTaskData(List<MatchstickTaskData> taskDataList) 
	{
		this.taskDataList = order(taskDataList);
	}
	
	/* Order in reverse (last taskData first) */
	protected List<MatchstickTaskData> order(List<MatchstickTaskData> taskDataList)
	{
		return taskDataList.stream()
				.sorted(Comparator.comparingLong(MatchstickTaskData::getId).reversed())
				.collect(Collectors.toList());	
	}
	
	public Optional<MatchstickTaskData> lastTaskData()
	{
		return (taskDataList.isEmpty()) ? Optional.empty() : Optional.of(taskDataList.get(0));
	}
	
}
