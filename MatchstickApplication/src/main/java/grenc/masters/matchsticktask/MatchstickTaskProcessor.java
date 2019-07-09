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
import grenc.simpleton.annotation.Bean;
import grenc.simpleton.annotation.InsertBean;


@Bean
public class MatchstickTaskProcessor
{
	@InsertBean
	private TaskSessionAssist taskSessionAssist;
	@InsertBean
	private TaskDataAssist taskDataAssist;
	@InsertBean
	private EquationAssist equationAssist;
	@InsertBean
	private EquationSolutionsSelector equationTypeSelector;
	
	
	public TaskSession taskSessionToUse(Session session)
	{
		return taskSessionAssist.getTaskSessionToUse(session, TaskType.matchstick);
	}

	public MatchstickExperimentPhase nextPhase(TaskSession taskSession)
	{
		return equationTypeSelector.phaseForTaskNumber(taskSession, newTaskNumber(taskSession));
	}
	public MatchstickGroup matchstickGroupType(Session session)
	{
		return taskSessionAssist.getTaskSessionToUse(session, TaskType.matchstick).getMatchstickGroup();
	}
	
	public MatchstickTaskProcessorReturn prepareNewMatchstickTask(TaskSession taskSession)
	{
		MatchstickTaskProcessorReturn newTaskResult = new MatchstickTaskProcessorReturn();
		int fullTaskNumber = newTaskNumber(taskSession);
		int testTaskNumber = fullTaskNumber - totalNumberOfTasksForObervingAndLearning(taskSession);
		
		newTaskResult.newTaskNumber = testTaskNumber;
		newTaskResult.totalNumberOfTasks = totalNumberOfTasks(taskSession) - totalNumberOfTasksForObervingAndLearning(taskSession);
		
		if (newTaskResult.newTaskNumber == 1)
			newTaskResult.pauseAtStart = true;
		else
			newTaskResult.pauseAtStart = false;
		
		MatchstickTaskStatus lastStatus = taskDataAssist.statusOfLastTask(taskSession);
		if (lastStatus != null && lastStatus.equals(MatchstickTaskStatus.restarted))
		{
			newTaskResult.newEquation = equationAssist.getLastUsedEquation(taskSession);
			newTaskResult.pauseAtStart = false;
			newTaskResult.continueWithTime = taskDataAssist.timeOfLastTask(taskSession);
		}
		else
		{
			if (lastStatus != null && lastStatus.equals(MatchstickTaskStatus.stopped))
			{
				newTaskResult.pauseAtStart = true;
			}
			
			MatchstickExperimentPhase experimentPhase = equationTypeSelector.phaseForTaskNumber(taskSession, fullTaskNumber);
			if (MatchstickExperimentPhase.TestingPhase_OnlyOriginalStrategy.equals(experimentPhase) 
				|| MatchstickExperimentPhase.TestingPhase_OnlyOppositeStrategy.equals(experimentPhase))
				newTaskResult.restriction = SolvableRestriction.ONE_MOVE_ONLY;
			else
				newTaskResult.restriction = SolvableRestriction.MINIMUM_MOVES;
			
			EquationSolutionsGroupType equationType = equationTypeSelector.findNextSolutionGroup(taskSession, newTaskNumber(taskSession));
			int taskNumber = newTaskNumberForLocalPhase(taskSession);
			System.out.println("New equation for task number '" + newTaskResult.newTaskNumber + " (" + newTaskNumber(taskSession) + ")" + "' [" + equationType + ", " + taskNumber + "]");
			newTaskResult.newEquation = equationAssist.getNextEquation(equationType, taskNumber);
		}
		
		return newTaskResult;
	}
	
	// Matchstick Observe
	public List<Video> prepareNewObserveMatchstickTask(TaskSession taskSession) {
		int taskNumber = newTaskNumberForLocalPhase(taskSession);
		EquationSolutionsGroupType videoType = equationTypeSelector.findNextSolutionGroup(taskSession, taskNumber);
		return new VideoSelectAssist().videoForTypeAndNumber(videoType, taskNumber);
	}
	
	// Matchstick Learn
	public String prepareNewLearnMatchstickTask(TaskSession taskSession) {
		int taskNumber = newTaskNumberForLocalPhase(taskSession);
		EquationSolutionsGroupType equationType = equationTypeSelector.findNextSolutionGroup(taskSession, taskNumber);
		return new LearnEquationAssist().equationCommandForTypeAndNumber(equationType, taskNumber);
	}
	
	
	
	public int newTaskNumber(TaskSession taskSession)
	{
		return taskDataAssist.newTaskNumber(taskSession);
	}
	public int totalNumberOfTasks(TaskSession taskSession)
	{
		return taskDataAssist.totalNumberOfTasks(taskSession);
	}
	public int totalNumberOfTasksForObervingAndLearning(TaskSession taskSession)
	{
		return taskDataAssist.getNoTasksForPhase(taskSession, MatchstickExperimentPhase.LearningPhase_Showing) + 
				taskDataAssist.getNoTasksForPhase(taskSession, MatchstickExperimentPhase.LearningPhase_Solving);
	}
	
	public int newTaskNumberForLocalPhase(TaskSession taskSession)
	{
		return taskDataAssist.newTaskNumber(taskSession) - taskDataAssist.getNoTasksUpToPhase(taskSession, nextPhase(taskSession));
	}
	public int totalNumberOfTasksForNextPhase(TaskSession taskSession)
	{
		return taskDataAssist.getNoTasksForPhase(taskSession, nextPhase(taskSession));
	}
	
	public boolean isCurrentTaskSessionFinished(TaskSession taskSession)
	{
		return taskDataAssist.isFinished(taskSession);
	}
	
	public void finishCurrentTaskSessionIfApplicable(TaskSession taskSession)
	{
		taskDataAssist.finishItIfApplicable(taskSession);
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
