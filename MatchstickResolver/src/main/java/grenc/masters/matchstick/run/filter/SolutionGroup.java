package grenc.masters.matchstick.run.filter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum SolutionGroup
{
	group_1N_2N_3N,
	group_1N_2N,
	group_1N,
	group_1M,
	group_1O,
	group_1O_2O,
	group_1O_2O_3O,
	group_1X_2N_3N,
	group_1X_2N,
	group_1X_2M,
	group_1X_2O,
	group_1X_2O_3O
	;
	
	private static final List<SolutionGroup> groupsAsList = Arrays.asList(SolutionGroup.values());
	
	private boolean isContained(String name)
	{
		return name.contains(this.name());
	}
	
	/**
	 * Finds the longest matching group
	 */
	public static SolutionGroup findTheBestGroup(final String name)
	{
		List<SolutionGroup> possibleGroups = groupsAsList.stream().filter(group -> group.isContained(name)).collect(Collectors.toList());
		SolutionGroup result = findWithLongestName(possibleGroups);
		if (result == null)
			throw new RuntimeException("Could not find a solution group for name: " + name);
		return result;
	}
	private static SolutionGroup findWithLongestName(List<SolutionGroup> groups)
	{
		SolutionGroup best = null;
		for (SolutionGroup group : groups)
		{
			if (best == null || best.name().length() < group.name().length())
				best = group;
		}
		return best;
	}
}
