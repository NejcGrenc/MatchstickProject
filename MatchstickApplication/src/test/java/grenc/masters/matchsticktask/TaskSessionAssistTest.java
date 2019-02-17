package grenc.masters.matchsticktask;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import grenc.masters.database.dao.MatchstickTaskDataDAO;
import grenc.masters.database.dao.TaskSessionDAO;
import grenc.masters.database.entities.Session;
import grenc.masters.database.entities.TaskSession;
import grenc.masters.matchsticktask.assistant.GroupSelectAssist;
import grenc.masters.matchsticktask.assistant.TaskSessionAssist;
import grenc.masters.matchsticktask.type.MatchstickGroup;
import grenc.masters.properties.TaskSessionProperty;;


public class TaskSessionAssistTest
{
	private static final boolean FINISHED = true;
	private static final boolean UNFINISHED = false;
	
	private TaskSessionDAO taskSessionDAO;
	private MatchstickTaskDataDAO matchstickTaskDataDAO;
	private GroupSelectAssist groupSelectAssist;
	
	// Subject
	TaskSessionAssist assist;
	
	Session session;
	String taskType;
	
//	@Before
//	public void setup()
//	{
//		this.taskSessionDAO = mock(TaskSessionDAO.class);
//		this.matchstickTaskDataDAO = mock(MatchstickTaskDataDAO.class);
//		this.groupSelectAssist = mock(GroupSelectAssist.class);
//	 
//	    // Mock setup data
//		session = mock(Session.class);
//		when(session.getId()).thenReturn(12);
//		taskType = TaskSessionProperty.matchstick.name();
//		
//		when(matchstickTaskDataDAO.findAllTaskForSessionId(anyInt()))
//			.thenReturn(Collections.emptyList());
//	}
//	
//	protected void setupAssistWithTaskSessions(List<TaskSession> taskSessions) 
//	{
//		when(taskSessionDAO.findAllTaskForSessionIdAndTaskType(eq(session.getId()), eq(taskType)))
//			.thenReturn(taskSessions);
//		assist = new TaskSessionAssist(session, taskType, taskSessionDAO, matchstickTaskDataDAO, groupSelectAssist);
//	}
//	
//	
//	@Test
//	public void shouldGetUnfinishedTaskSession()
//	{
//		// setup: with existing unfinished session
//		TaskSession existingSession = createTaskSession(UNFINISHED);
//		setupAssistWithTaskSessions(Arrays.asList(existingSession));
//		
//		// when: call for session is made
//		OngoingTaskSession returnedSession = assist.getUnfinishedOrCreateTaskSession();
//		
//		// then: returns unfinished session
//		assertEquals(existingSession, returnedSession.taskSession());
//	}
//	
//	@Test
//	public void shouldReturnLastUnfinishedTaskSessionWhereThereAreMore()
//	{
//		// setup: existing unfinished sessions
//		TaskSession newestUnfinishedSession = createTaskSession(UNFINISHED);
//		TaskSession oldUnfinishedSession = createTaskSession(UNFINISHED);
//		setupAssistWithTaskSessions(Arrays.asList(newestUnfinishedSession, oldUnfinishedSession));
//		
//		// when: call for session is made
//		OngoingTaskSession returnedSession = assist.getUnfinishedOrCreateTaskSession();
//		
//		// then: returns last unfinished session
//		assertEquals(newestUnfinishedSession, returnedSession.taskSession());
//	}
//	
//	@Test
//	public void shouldCreateNewTaskSessionIfLastIsComplete()
//	{
//		// setup: existing unfinished sessions
//		TaskSession newestFinishedSession = createTaskSession(FINISHED);
//		TaskSession oldUnfinishedSession = createTaskSession(UNFINISHED);
//		setupAssistWithTaskSessions(Arrays.asList(newestFinishedSession, oldUnfinishedSession));
//		
//		// and
//		prepareCreateNewTaskSession();
//		
//		// when: call for session is made
//		OngoingTaskSession returnedSession = assist.getUnfinishedOrCreateTaskSession();
//		
//		// then: 
//		assertCreateNewTaskSession();
//		
//		// and: returns new unfinished session
//		assertNotNull(returnedSession);
//		assertFalse(returnedSession.taskSession().isComplete());
//	}
//	
//	@Test
//	public void shouldCreateNewTaskSessionIfThereAreNone()
//	{
//		// setup: no existing sessions
//		setupAssistWithTaskSessions(Collections.emptyList());
//		
//		// and
//		prepareCreateNewTaskSession();
//		
//		// when: call for session is made
//		OngoingTaskSession returnedSession = assist.getUnfinishedOrCreateTaskSession();
//		
//		// then: 
//		assertCreateNewTaskSession();
//		
//		// and: returns valid unfinished session
//		assertNotNull(returnedSession);
//		assertFalse(returnedSession.taskSession().isComplete());
//	}
	
	
	private TaskSession createTaskSession(boolean complete)
	{
		TaskSession taskSession = new TaskSession();
		taskSession.setComplete(complete);
		taskSession.setMatchstickGroup(MatchstickGroup.group_A);
		return taskSession;
	}
	
	private void prepareCreateNewTaskSession()
	{	
		// prepare: database call
		TaskSession newTaskSession = createTaskSession(UNFINISHED);
		newTaskSession.setId(10);

		when(taskSessionDAO.insert(eq(session.getId()), eq(taskType), any(), eq(MatchstickGroup.group_A.name()), eq(false), any()))
				.thenReturn(newTaskSession);
		
		// and: new group calculation
		when(groupSelectAssist.selectNewGroup()).thenReturn(MatchstickGroup.group_A);
	}
	private void assertCreateNewTaskSession()
	{
		// then: verify insert call
		verify(taskSessionDAO).insert(eq(session.getId()), eq(taskType), any(), eq(MatchstickGroup.group_A.name()), eq(false), any());	
	}
	
}
