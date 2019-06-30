package grenc.masters.matchsticktask.assistant.equations;

import grenc.masters.database.entities.TaskSession;
import grenc.masters.database.equationgroups.EquationSolutionsGroupType;
import grenc.masters.matchsticktask.type.MatchstickExperimentPhase;
import grenc.masters.matchsticktask.type.MatchstickGroup;
import grenc.simpleton.annotation.Bean;

/**
 * Based on a file in same package 'Experimental_design_NG_MG.pdf',
 * which defines the task structure,
 * select Equation-Solution types to be solved next in the experiment.
 */
@Bean
public class EquationSolutionsSelector
{
	
	public EquationSolutionsGroupType findNextSolutionGroup(TaskSession currentTaskSession, int taskNumber)
	{
		return equationStyleForPhase(currentTaskSession, phaseForTaskNumber(currentTaskSession, taskNumber));
	}
	
	
	// Remember: Task numbers start with 1
	public MatchstickExperimentPhase phaseForTaskNumber(TaskSession currentTaskSession, int number)
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
	
	public EquationSolutionsGroupType equationStyleForPhase(TaskSession currentTaskSession, MatchstickExperimentPhase phase)
	{
		MatchstickGroup group = currentTaskSession.getMatchstickGroup();
		switch (group)
		{
			case group_A:
			case group_AB_strategyA:
			case group_0_strategyA:
				switch (phase)
				{
					case LearningPhase_Showing: 				   return EquationSolutionsGroupType.group_1N;
					case LearningPhase_Solving: 				   return EquationSolutionsGroupType.group_1N;
					case TestingPhase_OnlyOriginalStrategy: 	   return EquationSolutionsGroupType.group_1MO;
					case TestingPhase_OriginalStrategyOptimal:     return EquationSolutionsGroupType.group_1N_2N;
					case TestingPhase_BothStrategiesOptimal: 	   return EquationSolutionsGroupType.group_1MO;
					case TestingPhase_OppositeStrategyOptimal:     return EquationSolutionsGroupType.group_1O_2O;
					case TestingPhase_OnlyOppositeStrategy:        return EquationSolutionsGroupType.group_1O_2O;

				}
			case group_B:
			case group_AB_strategyB:
			case group_0_strategyB:
				switch (phase)
				{
					case LearningPhase_Showing: 				   return EquationSolutionsGroupType.group_1O;
					case LearningPhase_Solving: 				   return EquationSolutionsGroupType.group_1O;
					case TestingPhase_OnlyOriginalStrategy: 	   return EquationSolutionsGroupType.group_1MO;
					case TestingPhase_OriginalStrategyOptimal:     return EquationSolutionsGroupType.group_1N_2N;
					case TestingPhase_BothStrategiesOptimal: 	   return EquationSolutionsGroupType.group_1MO;
					case TestingPhase_OppositeStrategyOptimal:     return EquationSolutionsGroupType.group_1O_2O;
					case TestingPhase_OnlyOppositeStrategy:        return EquationSolutionsGroupType.group_1O_2O;
				}
			case test:
				switch (phase)
				{
					case LearningPhase_Showing: 				   return EquationSolutionsGroupType.group_1O;
					case LearningPhase_Solving: 				   return EquationSolutionsGroupType.group_1O;
					case TestingPhase_OnlyOriginalStrategy: 	   return EquationSolutionsGroupType.group_1MO;
					case TestingPhase_OriginalStrategyOptimal:     return EquationSolutionsGroupType.group_1N_2N;
					case TestingPhase_BothStrategiesOptimal: 	   return EquationSolutionsGroupType.group_1MO;
					case TestingPhase_OppositeStrategyOptimal:     return EquationSolutionsGroupType.group_1O_2O;
					case TestingPhase_OnlyOppositeStrategy:        return EquationSolutionsGroupType.group_1O_2O;
				}
		}
		return null;
	}
	 
}
