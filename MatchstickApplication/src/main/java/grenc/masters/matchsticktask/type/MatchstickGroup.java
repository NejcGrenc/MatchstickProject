package grenc.masters.matchsticktask.type;

import java.util.Arrays;
import java.util.List;

public enum MatchstickGroup
{
	group_A  (4, 10),
	group_B  (4, 10),
	group_AB (8, 10),
	group_0  (0, 10),
	test	 (0, 2)
	;
	
	private int noLearningTasks;
	private int noTestingTasks;

	private MatchstickGroup(int noLearningTasks, int noTestingTasks)
	{
		this.noLearningTasks = noLearningTasks;
		this.noTestingTasks = noTestingTasks;
	}
	
	public static List<MatchstickGroup> nonTestGroups()
	{
		return Arrays.asList(group_A, group_B, group_AB, group_0);
	}
	
	public int getNoLearningTasks()
	{
		return noLearningTasks;
	}
	
	public int getNoTestingTasks()
	{
		return noTestingTasks;
	}
	
	public int getNoTasks()
	{
		return noLearningTasks + noTestingTasks;
	}
}
