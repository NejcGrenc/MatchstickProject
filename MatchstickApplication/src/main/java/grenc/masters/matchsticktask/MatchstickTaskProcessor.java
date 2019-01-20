package grenc.masters.matchsticktask;

import grenc.masters.database.equationgroups.EquationSolutionsGroupType;
import grenc.masters.entities.Session;
import grenc.masters.entities.TaskSession;
import grenc.masters.matchsticktask.assistant.EquationAssist;
import grenc.masters.matchsticktask.assistant.TaskDataAssist;
import grenc.masters.matchsticktask.assistant.TaskSessionAssist;
import grenc.masters.matchsticktask.assistant.equations.EquationSolutionsSelector;
import grenc.masters.matchsticktask.type.MatchstickTaskStatus;
import grenc.masters.matchsticktask.type.TaskType;


public class MatchstickTaskProcessor
{
	private TaskSessionAssist taskSessionAssist;
	
	private Session session;
	
	private TaskSession taskSessionToUse;  // Lazy load
	private TaskDataAssist taskDataAssist;
	private EquationAssist equationAssist;
	private EquationSolutionsSelector equationTypeSelector;
	
	public MatchstickTaskProcessor(Session session)
	{
		this.session = session;
		this.taskSessionAssist = new TaskSessionAssist(session, TaskType.matchstick);
		
		this.taskSessionToUse = null;
	}

	
	public Session getCurrentSession()
	{
		return session;
	}
	
	public TaskSession taskSessionToUse()
	{
		if (taskSessionToUse == null)
		{
			taskSessionToUse = taskSessionAssist.getTaskSessionToUse();		
			this.taskDataAssist = new TaskDataAssist(taskSessionToUse);
			this.equationTypeSelector = new EquationSolutionsSelector(taskSessionToUse);
			this.equationAssist = new EquationAssist(taskSessionToUse);
		}
		return taskSessionToUse;
	}
	
	private TaskDataAssist taskDataAssist()
	{
		if (taskDataAssist == null)
			taskSessionToUse();
		return taskDataAssist;
	}
	private EquationSolutionsSelector equationSelect()
	{
		if (equationTypeSelector == null)
			taskSessionToUse();
		return equationTypeSelector;
	}
	private EquationAssist equationAssist()
	{
		if (equationAssist == null)
			taskSessionToUse();
		return equationAssist;
	}
	
	public int newTaskNumber()
	{
		return taskDataAssist().newTaskNumber();
	}	
	public int totalNumberOfTasks()
	{
		return taskDataAssist().totalNumberOfTasks();
	}
	
	public String newEquation()
	{
		MatchstickTaskStatus lastStatus = taskDataAssist().statusOfLastTask();
		if (lastStatus != null && lastStatus.equals(MatchstickTaskStatus.restarted))
		{
			return equationAssist().getLastUsedEquation();
		}
		
		int newtaskNumber = newTaskNumber();
		EquationSolutionsGroupType equationType = equationSelect().findNextSolutionGroup(newtaskNumber);
		return equationAssist().getNextEquation(equationType);
	}
	
	public boolean isCurrentTaskSessionFinished()
	{
		return taskDataAssist().isFinished();
	}
	
	public void finishCurrentTaskSessionIfApplicable()
	{
		taskDataAssist().finishItIfApplicable();
	}
}
