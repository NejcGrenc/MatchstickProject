package grenc.masters.matchsticktask.assistant.equations;

import grenc.masters.database.entities.TaskSession;
import grenc.masters.database.equationgroups.EquationSolutionsGroupType;
import grenc.masters.matchsticktask.type.MatchstickExperimentPhase;
import grenc.masters.matchsticktask.type.MatchstickGroup;

/**
 * Based on a file in same package 'Experimental_design_NG_MG.pdf',
 * which defines the task structure,
 * select Equation-Solution types to be solved next in the experiment.
 */
public class EquationSolutionsSelector
{
	private TaskSession currentTaskSession;
	
	public EquationSolutionsSelector (TaskSession taskSession)
	{
		this.currentTaskSession = taskSession;
	}
	
	public EquationSolutionsGroupType findNextSolutionGroup(int taskNumber)
	{
		return equationStyleForPhase(phaseForTaskNumber(taskNumber));
	}
	
	
	// Remember: Task numbers start with 1
	public MatchstickExperimentPhase phaseForTaskNumber(int number)
	{
		MatchstickGroup group = currentTaskSession.getMatchstickGroup();
		for (MatchstickExperimentPhase phase : MatchstickExperimentPhase.orderedAll())
		{
			number -= group.getNoTasksForPhase(phase);
			if (number <= 0)
				return phase;
		}
		throw new RuntimeException("No phase found for task number " + number + " for group " + group.name());
	}
	
	EquationSolutionsGroupType equationStyleForPhase(MatchstickExperimentPhase phase)
	{
		MatchstickGroup group = currentTaskSession.getMatchstickGroup();
		switch (group)
		{
			case group_A:
				switch (phase)
				{
					case LearningPhase_Showing: 				   return EquationSolutionsGroupType.group_1N;
					case LearningPhase_Solving: 				   return EquationSolutionsGroupType.group_1N;
					case TestingPhase_MixedEquations: 			   return EquationSolutionsGroupType.group_1MO;
					case TestingPhase_OriginalEquationsOptimal:    return EquationSolutionsGroupType.group_1N_2N;
					case TestingPhase_OriginalEquationsSuboptimal: return EquationSolutionsGroupType.group_1MO;
					case TestingPhase_OppositeEquationsOptimal:    return EquationSolutionsGroupType.group_1O_2O;
				}
			case group_B:
				switch (phase)
				{
					case LearningPhase_Showing: 				   return EquationSolutionsGroupType.group_1O;
					case LearningPhase_Solving: 				   return EquationSolutionsGroupType.group_1O;
					case TestingPhase_MixedEquations: 			   return EquationSolutionsGroupType.group_1MO;
					case TestingPhase_OriginalEquationsOptimal:    return EquationSolutionsGroupType.group_1O_2O;
					case TestingPhase_OriginalEquationsSuboptimal: return EquationSolutionsGroupType.group_1MO;
					case TestingPhase_OppositeEquationsOptimal:    return EquationSolutionsGroupType.group_1N_2N;
				}
			case group_AB:
				switch (phase)
				{
					case LearningPhase_Showing: 				   return EquationSolutionsGroupType.group_1O;
					case LearningPhase_Solving: 				   return EquationSolutionsGroupType.group_1O;
					case TestingPhase_MixedEquations: 			   return EquationSolutionsGroupType.group_1MO;
					case TestingPhase_OriginalEquationsOptimal:    return EquationSolutionsGroupType.group_1MO;
					case TestingPhase_OriginalEquationsSuboptimal: return EquationSolutionsGroupType.group_1MO;
					case TestingPhase_OppositeEquationsOptimal:    return EquationSolutionsGroupType.group_1MO;
				}
			case group_0:
				switch (phase)
				{
					case LearningPhase_Showing: 				   return null;
					case LearningPhase_Solving: 				   return null;
					case TestingPhase_MixedEquations: 			   return EquationSolutionsGroupType.group_1MO;
					case TestingPhase_OriginalEquationsOptimal:    return EquationSolutionsGroupType.group_1MO;
					case TestingPhase_OriginalEquationsSuboptimal: return EquationSolutionsGroupType.group_1MO;
					case TestingPhase_OppositeEquationsOptimal:    return EquationSolutionsGroupType.group_1MO;
				}
			case test:
				switch (phase)
				{
					case LearningPhase_Showing: 				   return null;
					case LearningPhase_Solving: 				   return null;
					case TestingPhase_MixedEquations: 			   return null;
					case TestingPhase_OriginalEquationsOptimal:    return EquationSolutionsGroupType.group_1O;
					case TestingPhase_OriginalEquationsSuboptimal: return EquationSolutionsGroupType.group_1N;
					case TestingPhase_OppositeEquationsOptimal:    return null;
				}
		}
		return null;
	}
	 
	
}
