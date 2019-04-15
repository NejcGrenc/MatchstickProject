package grenc.masters.matchsticktask;

import java.util.List;

import grenc.masters.database.entities.Session;
import grenc.masters.database.entities.TaskSession;
import grenc.masters.database.equationgroups.EquationSolutionsGroupType;
import grenc.masters.matchsticktask.assistant.EquationAssist;
import grenc.masters.matchsticktask.assistant.TaskDataAssist;
import grenc.masters.matchsticktask.assistant.TaskSessionAssist;
import grenc.masters.matchsticktask.assistant.equations.EquationSolutionsSelector;
import grenc.masters.matchsticktask.assistant.speciffic.LearnEquationAssist;
import grenc.masters.matchsticktask.assistant.speciffic.VideoSelectAssist;
import grenc.masters.matchsticktask.type.MatchstickExperimentPhase;
import grenc.masters.matchsticktask.type.MatchstickGroup;
import grenc.masters.matchsticktask.type.MatchstickTaskStatus;
import grenc.masters.matchsticktask.type.SolvableRestriction;
import grenc.masters.matchsticktask.type.TaskType;
import grenc.masters.resources.Video;


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
	
	public MatchstickExperimentPhase nextPhase()
	{
		return equationSelect().phaseForTaskNumber(newTaskNumber());
	}
	public MatchstickGroup matchstickGroupType()
	{
		return taskSessionToUse().getMatchstickGroup();
	}
	
	public MatchstickTaskProcessorReturn prepareNewMatchstickTask()
	{
		MatchstickTaskProcessorReturn newTaskResult = new MatchstickTaskProcessorReturn();
		newTaskResult.newTaskNumber = newTaskNumber() - totalNumberOfTasksForObervingAndLearning();
		newTaskResult.totalNumberOfTasks = totalNumberOfTasks() - totalNumberOfTasksForObervingAndLearning();
		
		if (newTaskResult.newTaskNumber == 1)
			newTaskResult.pauseAtStart = true;
		else
			newTaskResult.pauseAtStart = false;
		
		MatchstickTaskStatus lastStatus = taskDataAssist().statusOfLastTask();
		if (lastStatus != null && lastStatus.equals(MatchstickTaskStatus.restarted))
		{
			newTaskResult.newEquation = equationAssist().getLastUsedEquation();
			newTaskResult.pauseAtStart = false;
			newTaskResult.continueWithTime = taskDataAssist().timeOfLastTask();
		}
		else
		{
			if (lastStatus != null && lastStatus.equals(MatchstickTaskStatus.stopped))
			{
				newTaskResult.pauseAtStart = true;
			}
			
			MatchstickExperimentPhase experimentPhase = equationSelect().phaseForTaskNumber(newTaskResult.newTaskNumber);
			if (MatchstickExperimentPhase.TestingPhase_OnlyOriginalStrategy.equals(experimentPhase) 
				|| MatchstickExperimentPhase.TestingPhase_OnlyOppositeStrategy.equals(experimentPhase))
				newTaskResult.restriction = SolvableRestriction.ONE_MOVE_ONLY;
			else
				newTaskResult.restriction = SolvableRestriction.MINIMUM_MOVES;
			
			EquationSolutionsGroupType equationType = equationSelect().findNextSolutionGroup(newTaskResult.newTaskNumber);
			newTaskResult.newEquation = equationAssist().getNextEquation(equationType);
		}
		
		return newTaskResult;
	}
	
	// Matchstick Observe
	public List<Video> prepareNewObserveMatchstickTask() {
		int taskNumber = newTaskNumberForLocalPhase();
		EquationSolutionsGroupType videoType = equationSelect().findNextSolutionGroup(taskNumber);
		return new VideoSelectAssist().videoForTypeAndNumber(videoType, taskNumber);
	}
	
	// Matchstick Learn
	public String prepareNewLearnMatchstickTask() {
		int taskNumber = newTaskNumberForLocalPhase();
		EquationSolutionsGroupType equationType = equationSelect().findNextSolutionGroup(taskNumber);
		return new LearnEquationAssist().equationCommandForTypeAndNumber(equationType, taskNumber);
	}
	
	
	
	public int newTaskNumber()
	{
		return taskDataAssist().newTaskNumber();
	}
	public int totalNumberOfTasks()
	{
		return taskDataAssist().totalNumberOfTasks();
	}
	public int totalNumberOfTasksForObervingAndLearning()
	{
		return taskDataAssist().getNoTasksForPhase(MatchstickExperimentPhase.LearningPhase_Showing) + 
				taskDataAssist().getNoTasksForPhase(MatchstickExperimentPhase.LearningPhase_Solving);
	}
	
	public int newTaskNumberForLocalPhase()
	{
		return taskDataAssist().newTaskNumber() - taskDataAssist().getNoTasksUpToPhase(nextPhase());
	}
	public int totalNumberOfTasksForNextPhase()
	{
		return taskDataAssist().getNoTasksForPhase(nextPhase());
	}
	
	public boolean isCurrentTaskSessionFinished()
	{
		return taskDataAssist().isFinished();
	}
	
	public void finishCurrentTaskSessionIfApplicable()
	{
		taskDataAssist().finishItIfApplicable();
	}
	
	
	public class MatchstickTaskProcessorReturn
	{
		public int newTaskNumber;
		public int totalNumberOfTasks;
		public String newEquation;
		public SolvableRestriction restriction;
		public boolean pauseAtStart;
		public long continueWithTime = 0l;
	}

}
