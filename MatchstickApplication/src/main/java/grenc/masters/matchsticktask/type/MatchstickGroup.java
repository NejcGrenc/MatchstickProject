package grenc.masters.matchsticktask.type;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public enum MatchstickGroup
{
	group_A  (2, 2, 2, 2, 2, 4),
	group_B  (2, 2, 2, 2, 2, 4),
	group_AB (4, 4, 2, 2, 2, 4),
	group_0  (0, 0, 2, 2, 2, 4),
	test	 (1, 1, 1, 1, 1, 1)
	;
	

	private HashMap<MatchstickExperimentPhase, Integer> tasksForPhase;

	private MatchstickGroup(int... noTasksForEachPhase)
	{
		this.tasksForPhase = new HashMap<>();
		int i = 0;
		this.tasksForPhase.put(MatchstickExperimentPhase.LearningPhase_Showing, noTasksForEachPhase[i++]);
		this.tasksForPhase.put(MatchstickExperimentPhase.LearningPhase_Solving, noTasksForEachPhase[i++]);
		this.tasksForPhase.put(MatchstickExperimentPhase.TestingPhase_MixedEquations, noTasksForEachPhase[i++]);
		this.tasksForPhase.put(MatchstickExperimentPhase.TestingPhase_OriginalEquationsOptimal, noTasksForEachPhase[i++]);
		this.tasksForPhase.put(MatchstickExperimentPhase.TestingPhase_OriginalEquationsSuboptimal, noTasksForEachPhase[i++]);
		this.tasksForPhase.put(MatchstickExperimentPhase.TestingPhase_OppositeEquationsOptimal, noTasksForEachPhase[i++]);
	}
	
	public static List<MatchstickGroup> nonTestGroups()
	{
		return Arrays.asList(group_A, group_B, group_AB, group_0);
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
