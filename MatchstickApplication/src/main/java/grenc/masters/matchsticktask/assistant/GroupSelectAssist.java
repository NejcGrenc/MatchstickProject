package grenc.masters.matchsticktask.assistant;

import java.util.HashMap;
import java.util.List;

import grenc.masters.database.TaskSessionDAO;
import grenc.masters.entities.TaskSession;
import grenc.masters.matchsticktask.type.MatchstickGroup;
import grenc.masters.matchsticktask.type.TaskType;

public class GroupSelectAssist 
{
	private static final MatchstickGroup defaultGroup = MatchstickGroup.group_AB;

	private TaskSessionDAO taskSessionDAO;
	
	GroupSelectAssist(TaskSessionDAO taskSessionDAO)
	{
		this.taskSessionDAO = taskSessionDAO;
	}
	public GroupSelectAssist()
	{
		this (TaskSessionDAO.getInstance());
	}

	
	// Find least used group
	public synchronized MatchstickGroup selectNewGroup()
	{
		HashMap<MatchstickGroup, Integer> previousGroups = countPreviousGroups();
		
		MatchstickGroup minUsedGroup = defaultGroup;
		int min = previousGroups.values().stream().mapToInt(Integer::intValue).min().getAsInt();
		for (MatchstickGroup group : MatchstickGroup.nonTestGroups())
			if (previousGroups.get(group) == min)
				minUsedGroup = group;
		
		return minUsedGroup;
	}
	
	private HashMap<MatchstickGroup, Integer> countPreviousGroups()
	{
		HashMap<MatchstickGroup, Integer> previousGroups = new HashMap<>();
		for (MatchstickGroup group : MatchstickGroup.nonTestGroups())
			previousGroups.put(group, 0);

		List<TaskSession> allMatchstickSessions = taskSessionDAO.findAllTaskForType(TaskType.matchstick.name());
		for (TaskSession taskSession : allMatchstickSessions)
		{
			MatchstickGroup group = taskSession.getMatchstickGroup();
			if (group == MatchstickGroup.test)
				continue;
			
			previousGroups.put(group, previousGroups.get(group) + 1);
		}
		return previousGroups;
	}
}
