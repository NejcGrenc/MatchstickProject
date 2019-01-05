package grenc.masters.matchstick.run.filter;

import java.util.List;

import grenc.masters.matchstick.objects.changes.EquationChangeSingle;


public enum Group
{

	groupAA ("group-N1-SN2", new RequirementsGroupAA()),
	groupAB ("group-N1-NO2", new RequirementsGroupAB()),
	groupBA ("group-O1-ON2", new RequirementsGroupBA()),
	groupBB ("group-O1-SO2", new RequirementsGroupBB()),
	groupCC ("group-ON12", new RequirementsGroupCC())
	;
	
	private String groupName;
	private GroupRequirements requirements;
	
	private Group (String groupName, GroupRequirements requirements)
	{
		this.groupName = groupName;
		this.requirements = requirements;
	}
	
	
	public String getGroupName()
	{
		return groupName;
	}
	
	public GroupRequirements getRequirements()
	{
		return requirements;
	}
	
	
	public boolean sufficesRequirements(List<EquationChangeSingle> equationSolutions)
	{
		return requirements.check(equationSolutions);
	}
	
}

