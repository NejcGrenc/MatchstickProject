package grenc.masters.matchsticktask.assistant;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

import grenc.masters.database.dao.TaskSessionDAO;
import grenc.masters.database.entities.TaskSession;
import grenc.masters.matchsticktask.type.MatchstickGroup;

public class GroupSelectAssistTest
{
	TaskSessionDAO taskSessionDAO = Mockito.mock(TaskSessionDAO.class);

	GroupSelectAssist groupSelectAssist = new GroupSelectAssist(taskSessionDAO);
	
	@Test
	public void shouldFindLeastUsed()
	{
		
		// Prepare test
		List<TaskSession> existingTaskSessions = new ArrayList<>();
		Mockito.doReturn(existingTaskSessions).when(taskSessionDAO).findAllTaskForType("matchstick");
		
		
		assertEquals(MatchstickGroup.group_A, groupSelectAssist.selectNewGroup());
		existingTaskSessions.add(newTaskSession(MatchstickGroup.group_A));
		
		assertEquals(MatchstickGroup.group_B, groupSelectAssist.selectNewGroup());
		existingTaskSessions.add(newTaskSession(MatchstickGroup.group_B));
		
		assertEquals(MatchstickGroup.group_AB_strategyA, groupSelectAssist.selectNewGroup());
		existingTaskSessions.add(newTaskSession(MatchstickGroup.group_AB_strategyA));
		
		assertEquals(MatchstickGroup.group_0_strategyA, groupSelectAssist.selectNewGroup());
		existingTaskSessions.add(newTaskSession(MatchstickGroup.group_0_strategyA));
		
		assertEquals(MatchstickGroup.group_A, groupSelectAssist.selectNewGroup());
		existingTaskSessions.add(newTaskSession(MatchstickGroup.group_A));
		
		assertEquals(MatchstickGroup.group_B, groupSelectAssist.selectNewGroup());
		existingTaskSessions.add(newTaskSession(MatchstickGroup.group_B));
		
		assertEquals(MatchstickGroup.group_AB_strategyB, groupSelectAssist.selectNewGroup());
		existingTaskSessions.add(newTaskSession(MatchstickGroup.group_AB_strategyB));
		
		
		assertEquals(MatchstickGroup.group_0_strategyB, groupSelectAssist.selectNewGroup());
		existingTaskSessions.add(newTaskSession(MatchstickGroup.group_0_strategyB));
		
		assertEquals(MatchstickGroup.group_A, groupSelectAssist.selectNewGroup());
		existingTaskSessions.add(newTaskSession(MatchstickGroup.group_A));
		assertEquals(MatchstickGroup.group_B, groupSelectAssist.selectNewGroup());
		// etc.
	}
	
	
	private TaskSession newTaskSession(MatchstickGroup group)
	{
		TaskSession taskSession = new TaskSession();
		taskSession.setMatchstickGroup(group);
		return taskSession;
	}
}
