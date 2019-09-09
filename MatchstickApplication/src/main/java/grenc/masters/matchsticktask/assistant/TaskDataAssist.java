package grenc.masters.matchsticktask.assistant;

import java.util.List;
import java.util.Optional;

import grenc.masters.database.dao.MatchstickTaskDataDAO;
import grenc.masters.database.dao.TaskSessionDAO;
import grenc.masters.database.entities.MatchstickTaskData;
import grenc.masters.database.entities.TaskSession;
import grenc.masters.matchsticktask.assistant.model.OrderedTaskData;
import grenc.masters.matchsticktask.type.MatchstickTaskStatus;
import grenc.simpleton.annotation.Bean;
import grenc.simpleton.annotation.InsertBean;

@Bean
public class TaskDataAssist {

	@InsertBean
	private TaskSessionDAO taskSessionDAO;
	@InsertBean
	private MatchstickTaskDataDAO matchstickTaskDataDAO;

	@InsertBean
	private TaskNumberAssist taskNumberAssist;

	
	public OrderedTaskData getOrderedTaskData(TaskSession taskSession)
	{
		List<MatchstickTaskData> taskDataList = matchstickTaskDataDAO.findAllTaskForSessionId(taskSession.getId());
		return new OrderedTaskData(taskDataList);
	}

	public boolean isFinished(TaskSession taskSession)
	{
		return taskSession.isComplete();
	}
	
	public void finishItIfApplicable(TaskSession taskSession)
	{
		if (! isFinished(taskSession) && shouldBeFinished(taskSession))
			taskSessionDAO.updateComplete(taskSession.getId(), true);
	}
	
	private boolean shouldBeFinished(TaskSession taskSession)
	{
		return (taskNumberAssist.newTaskNumber(taskSession) > taskNumberAssist.totalNumberOfTasks(taskSession));
	}

	
	public MatchstickTaskData lastStoredTaskData(TaskSession taskSession)
	{
		Optional<MatchstickTaskData> lastTaskDataOptional = getOrderedTaskData(taskSession).lastTaskData();
		if (! lastTaskDataOptional.isPresent())
			return null;	
		return lastTaskDataOptional.get();
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
}
