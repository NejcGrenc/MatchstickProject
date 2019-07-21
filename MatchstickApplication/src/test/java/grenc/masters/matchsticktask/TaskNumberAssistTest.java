package grenc.masters.matchsticktask;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

import grenc.masters.database.entities.MatchstickTaskData;
import grenc.masters.database.entities.TaskSession;
import grenc.masters.matchsticktask.assistant.TaskDataAssist;
import grenc.masters.matchsticktask.assistant.TaskNumberAssist;
import grenc.masters.matchsticktask.assistant.model.OrderedTaskData;
import grenc.masters.matchsticktask.type.MatchstickGroup;
import grenc.masters.matchsticktask.type.MatchstickTaskStatus;

public class TaskNumberAssistTest
{
	private TaskDataAssist taskDataAssist = Mockito.mock(TaskDataAssist.class);
	
	private TaskNumberAssist taskNumberAssist = new TaskNumberAssist(taskDataAssist);
	
	
	@Test
	public void shouldCalculateTaskNumbers_forSolved()
	{
		TaskSession someActiveTaskSession = new TaskSession();
		someActiveTaskSession.setMatchstickGroup(MatchstickGroup.group_A);
		
		//TestingPhase_OnlyOriginalStrategy
		MatchstickTaskData lastData = new MatchstickTaskData();
		lastData.setNumber(5);
		lastData.setPhaseNumber(1);
		lastData.setStatus(MatchstickTaskStatus.solved);
		mockTaskData(someActiveTaskSession, lastData);
		
		assertEquals(new Integer(5), taskNumberAssist.lastTaskNumber(someActiveTaskSession));
		assertEquals(new Integer(1), taskNumberAssist.lastTaskLocalNumber(someActiveTaskSession));
		assertEquals(6, taskNumberAssist.newTaskNumber(someActiveTaskSession));
		assertEquals(2, taskNumberAssist.newTaskLocalNumber(someActiveTaskSession));
	}
	
	@Test
	public void shouldCalculateTaskNumbers_forSolvedLastLearningPhase()
	{
		TaskSession someActiveTaskSession = new TaskSession();
		someActiveTaskSession.setMatchstickGroup(MatchstickGroup.group_A);
		
		//TestingPhase_OnlyOriginalStrategy
		MatchstickTaskData lastData = new MatchstickTaskData();
		lastData.setNumber(4);
		lastData.setPhaseNumber(4);
		lastData.setStatus(MatchstickTaskStatus.solved);
		mockTaskData(someActiveTaskSession, lastData);
		
		assertEquals(new Integer(4), taskNumberAssist.lastTaskNumber(someActiveTaskSession));
		assertEquals(new Integer(4), taskNumberAssist.lastTaskLocalNumber(someActiveTaskSession));
		assertEquals(5, taskNumberAssist.newTaskNumber(someActiveTaskSession));
		assertEquals(1, taskNumberAssist.newTaskLocalNumber(someActiveTaskSession));
	}
	
	@Test
	public void shouldCalculateTaskNumbers_forRestart()
	{
		TaskSession someActiveTaskSession = new TaskSession();
		someActiveTaskSession.setMatchstickGroup(MatchstickGroup.group_A);
		
		//TestingPhase_OnlyOriginalStrategy
		MatchstickTaskData lastData = new MatchstickTaskData();
		lastData.setNumber(5);
		lastData.setPhaseNumber(1);
		lastData.setStatus(MatchstickTaskStatus.restarted);
		mockTaskData(someActiveTaskSession, lastData);
		
		assertEquals(new Integer(5), taskNumberAssist.lastTaskNumber(someActiveTaskSession));
		assertEquals(new Integer(1), taskNumberAssist.lastTaskLocalNumber(someActiveTaskSession));
		assertEquals(5, taskNumberAssist.newTaskNumber(someActiveTaskSession));
		assertEquals(1, taskNumberAssist.newTaskLocalNumber(someActiveTaskSession));
	}
	
	
	private void mockTaskData(TaskSession taskSession, MatchstickTaskData data)
	{
		List<MatchstickTaskData> dataList = Collections.singletonList(data);
		Mockito.doReturn(new OrderedTaskData(dataList)).when(taskDataAssist).getOrderedTaskData(taskSession);
	}
	
}
