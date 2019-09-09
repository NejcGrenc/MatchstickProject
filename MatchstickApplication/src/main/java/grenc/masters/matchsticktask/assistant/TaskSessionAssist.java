package grenc.masters.matchsticktask.assistant;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import grenc.masters.database.dao.TaskSessionDAO;
import grenc.masters.database.entities.Session;
import grenc.masters.database.entities.TaskSession;
import grenc.masters.matchsticktask.assistant.model.OrderedTaskSessions;
import grenc.masters.matchsticktask.type.MatchstickGroup;
import grenc.masters.matchsticktask.type.TaskType;
import grenc.simpleton.annotation.Bean;
import grenc.simpleton.annotation.InsertBean;


@Bean
public class TaskSessionAssist 
{
	@InsertBean
	private TaskSessionDAO taskSessionDAO;
	@InsertBean
	private GroupSelectAssist groupSelectAssist;

	
	public OrderedTaskSessions getOrderedTaskSessions(Session session, TaskType taskType)
	{
		List<TaskSession> taskSessions = taskSessionDAO.findAllTaskForSessionIdAndTaskType(session.getId(), taskType.name());
		return new OrderedTaskSessions(taskSessions);
	}
	
	public TaskSession getTaskSessionToUse(Session session, TaskType taskType)
	{
		Optional<TaskSession> lastTaskSession = getOrderedTaskSessions(session, taskType).lastTaskSession();
		TaskSession taskSessionToUse;
		
		if (! lastTaskSession.isPresent())
		{
			taskSessionToUse = createNewTaskSession(session, taskType);
			System.out.println("Creating first session: " + taskSessionToUse);
		}	
		else if (lastTaskSession.get().isComplete())
		{
			taskSessionToUse = createNewTaskSession(session, taskType);
			System.out.println("Creating new session: " + taskSessionToUse);
		}
		else {
			taskSessionToUse = lastTaskSession.get();
			System.out.println("Selecting unfinished session: " + taskSessionToUse);
		}
		
		return taskSessionToUse;
	}
	
	public TaskSession createNewTaskSession(Session session, TaskType taskType)
	{
		MatchstickGroup newGroup = null;
		if (TaskType.matchstick.equals(taskType))
			newGroup = groupSelectAssist.selectNewGroup();
		
		String newGroupName = (newGroup != null) ? newGroup.toString() : null;
			
		return taskSessionDAO.insert(session.getId(), taskType.name(), 
				new Date().getTime(), newGroupName,
				false, null);		
	}

	
}
