package grenc.masters.matchsticktask.type;

import java.util.HashMap;

public enum MatchstickGroup
{
	group_A  (MatchstickGroupInitialStrategy.STRATEGY_A, MatchstickGroupLearnedStrategies.STRATEGY_A,  	   2, 2, 2, 2, 2, 2, 2),
	group_B  (MatchstickGroupInitialStrategy.STRATEGY_B, MatchstickGroupLearnedStrategies.STRATEGY_B,  	   2, 2, 2, 2, 2, 2, 2),
	group_AB_strategyA (MatchstickGroupInitialStrategy.STRATEGY_A, MatchstickGroupLearnedStrategies.BOTH,  4, 4, 2, 2, 2, 2, 2),
	group_AB_strategyB (MatchstickGroupInitialStrategy.STRATEGY_B, MatchstickGroupLearnedStrategies.BOTH,  4, 4, 2, 2, 2, 2, 2),
	group_0_strategyA  (MatchstickGroupInitialStrategy.STRATEGY_A, MatchstickGroupLearnedStrategies.NONE,  0, 0, 2, 2, 2, 2, 2),
	group_0_strategyB  (MatchstickGroupInitialStrategy.STRATEGY_B, MatchstickGroupLearnedStrategies.NONE,  0, 0, 2, 2, 2, 2, 2),
	;
	

	private MatchstickGroupInitialStrategy initialStrategy;
	private MatchstickGroupLearnedStrategies learnedStrategy;

	private HashMap<MatchstickExperimentPhase, Integer> tasksForPhase;

	private MatchstickGroup(MatchstickGroupInitialStrategy initialStrategy, MatchstickGroupLearnedStrategies learnedStrategy, int... noTasksForEachPhase)
	{
		this.initialStrategy = initialStrategy;
		this.learnedStrategy = learnedStrategy;
		
		this.tasksForPhase = new HashMap<>();
		int i = 0;
		this.tasksForPhase.put(MatchstickExperimentPhase.LearningPhase_Showing, noTasksForEachPhase[i++]);
		this.tasksForPhase.put(MatchstickExperimentPhase.LearningPhase_Solving, noTasksForEachPhase[i++]);
		this.tasksForPhase.put(MatchstickExperimentPhase.TestingPhase_OnlyOriginalStrategy, noTasksForEachPhase[i++]);
		this.tasksForPhase.put(MatchstickExperimentPhase.TestingPhase_OriginalStrategyOptimal, noTasksForEachPhase[i++]);
		this.tasksForPhase.put(MatchstickExperimentPhase.TestingPhase_BothStrategiesOptimal, noTasksForEachPhase[i++]);
		this.tasksForPhase.put(MatchstickExperimentPhase.TestingPhase_OppositeStrategyOptimal, noTasksForEachPhase[i++]);
		this.tasksForPhase.put(MatchstickExperimentPhase.TestingPhase_OnlyOppositeStrategy, noTasksForEachPhase[i++]);
	}
	
	
	public MatchstickGroupInitialStrategy getInitialStrategy() {
		return initialStrategy;
	}
	
	public MatchstickGroupLearnedStrategies getLearnedStrategy() {
		return learnedStrategy;
	}
	
	
	public int getNoTasksForPhase(MatchstickExperimentPhase phase)
	{
		return tasksForPhase.get(phase);
	}
	
	public int getNoTasks()
	{
		int total = 0;
		for (int i : tasksForPhase.values())
			total += i;
		return total;
	}
	
	public MatchstickExperimentPhase getPhaseForTaskNumber(int globalTaskNumber) {
		for (MatchstickExperimentPhase currPhase : MatchstickExperimentPhase.orderedAll())
		{			
			globalTaskNumber -= tasksForPhase.get(currPhase);
			if (globalTaskNumber <= 0)
			{			
				return currPhase;
			}
		}
		return null;
	}
	
	public int getNoTasksUpToPhase(MatchstickExperimentPhase phase)
	{
		int total = 0;
		for (MatchstickExperimentPhase currPhase : MatchstickExperimentPhase.orderedAll())
		{			
			if (currPhase.equals(phase)) break;
			total += tasksForPhase.get(currPhase);
		}
		return total;
	}
	
	public int getNoTasksObservingAndLearning()
	{
		return getNoTasksForPhase(MatchstickExperimentPhase.LearningPhase_Showing) + getNoTasksForPhase(MatchstickExperimentPhase.LearningPhase_Solving);
	}

}
