package grenc.masters.matchsticktask.type;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public enum MatchstickGroup
{
	group_A  (4, 10),
	group_B  (4, 10),
	group_AB (8, 10),
	group_0  (0, 10),
	test	 (0, 2)
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
	
}
