package grenc.masters.matchsticktask.assistant;

import java.util.HashMap;
import java.util.List;

import grenc.masters.database.dao.TaskSessionDAO;
import grenc.masters.database.entities.TaskSession;
import grenc.masters.matchsticktask.type.MatchstickGroup;
import grenc.masters.matchsticktask.type.TaskType;
import grenc.simpleton.annotation.Bean;
import grenc.simpleton.annotation.InsertBean;

@Bean
public class GroupSelectAssist 
{
	@InsertBean
	private TaskSessionDAO taskSessionDAO;
	
	public GroupSelectAssist() {}
	public GroupSelectAssist(TaskSessionDAO taskSessionDao) { this.taskSessionDAO = taskSessionDao; }
	
	
	// Find least used group
	public synchronized MatchstickGroup selectNewGroup()
	{
		MatchstickGroup minUsedGroup = MatchstickGroup.group_A;  // Just some default
		int minUsedGroupParticipants = Integer.MAX_VALUE;

		HashMap<MatchstickGroup, Integer> previousGroups = countPreviousGroups();
		
		if (previousGroups.get(MatchstickGroup.group_A) < minUsedGroupParticipants)
		{
			minUsedGroup = MatchstickGroup.group_A;
			minUsedGroupParticipants = previousGroups.get(MatchstickGroup.group_A);
		}
		if (previousGroups.get(MatchstickGroup.group_B) < minUsedGroupParticipants)
		{
			minUsedGroup = MatchstickGroup.group_B;
			minUsedGroupParticipants = previousGroups.get(MatchstickGroup.group_B);
		}
		if ((previousGroups.get(MatchstickGroup.group_AB_strategyA) + previousGroups.get(MatchstickGroup.group_AB_strategyB)) < minUsedGroupParticipants)
		{
			if (previousGroups.get(MatchstickGroup.group_AB_strategyA) <= previousGroups.get(MatchstickGroup.group_AB_strategyB))
			{
				minUsedGroup = MatchstickGroup.group_AB_strategyA;
				minUsedGroupParticipants = previousGroups.get(MatchstickGroup.group_AB_strategyA);
			}
			else 
			{
				minUsedGroup = MatchstickGroup.group_AB_strategyB;
				minUsedGroupParticipants = previousGroups.get(MatchstickGroup.group_AB_strategyB);
			}
		}
		if ((previousGroups.get(MatchstickGroup.group_0_strategyA) + previousGroups.get(MatchstickGroup.group_0_strategyB)) < minUsedGroupParticipants)
		{
			if (previousGroups.get(MatchstickGroup.group_0_strategyA) <= previousGroups.get(MatchstickGroup.group_0_strategyB))
			{
				minUsedGroup = MatchstickGroup.group_0_strategyA;
				minUsedGroupParticipants = previousGroups.get(MatchstickGroup.group_0_strategyA);
			}
			else 
			{
				minUsedGroup = MatchstickGroup.group_0_strategyB;
				minUsedGroupParticipants = previousGroups.get(MatchstickGroup.group_0_strategyB);
			}
		}
		
		return minUsedGroup;
	}
	
	private HashMap<MatchstickGroup, Integer> countPreviousGroups()
	{
		HashMap<MatchstickGroup, Integer> previousGroups = new HashMap<>();
		for (MatchstickGroup group : MatchstickGroup.values())
			previousGroups.put(group, 0);

		List<TaskSession> allMatchstickSessions = taskSessionDAO.findAllTaskForType(TaskType.matchstick.name());
		for (TaskSession taskSession : allMatchstickSessions)
		{
			MatchstickGroup group = taskSession.getMatchstickGroup();
			previousGroups.put(group, previousGroups.get(group) + 1);
		}
		return previousGroups;
	}
}
