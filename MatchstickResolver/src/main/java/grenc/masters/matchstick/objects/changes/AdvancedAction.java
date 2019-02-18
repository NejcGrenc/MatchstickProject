package grenc.masters.matchstick.objects.changes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import grenc.masters.matchstick.objects.main.Action;

public class AdvancedAction
{
	private static String match_add 	   = "+";
	private static String match_remove     = "-";
	private static String match_moveWithin = "w";
	
	private static String element_Comparator = "C";
	private static String element_Operator   = "O";
	private static String element_Numeral    = "N";
	
	private Action action;
	private List<String> addedTo;
	private List<String> removedFrom;
	private List<String> movedWithin;
	
	
	public AdvancedAction (Action action)
	{
		this.action = action;
		this.addedTo = new ArrayList<>();
		this.removedFrom = new ArrayList<>();
		this.movedWithin = new ArrayList<>();	
	}
	
	
	@Override
	public String toString()
	{
		String s = action.toString() + " (";
		s += addAllMatchList(match_add, addedTo, action.getAdded());
		s += ", ";
		s += addAllMatchList(match_remove, removedFrom, action.getRemoved());
		s += ", ";
		s += addAllMatchList(match_moveWithin, movedWithin, (action.getAdded() + action.getRemoved()) / 2);
		s += ")";
		return s;
	}
	private String addAllMatchList(String type, List<String> list, int maxElements)
	{
		String s = type;
		for (int i = 0; i < maxElements; i++)
		{
			if (i < list.size())
				s += list.get(i);
			else
				s += " ";
		}
		return s;
	}
	
	
	public AdvancedAction matchAddedToComparator()
	{
		addedTo.add(element_Comparator);
		return this;
	}
	public AdvancedAction matchAddedToOperator()
	{
		addedTo.add(element_Operator);
		return this;
	}
	public AdvancedAction matchAddedToNumeral()
	{
		addedTo.add(element_Numeral);
		return this;
	}
	public AdvancedAction matchRemovedFromComparator()
	{
		removedFrom.add(element_Comparator);
		return this;
	}
	public AdvancedAction matchRemovedFromOperator()
	{
		removedFrom.add(element_Operator);
		return this;
	}
	public AdvancedAction matchRemovedFromNumeral()
	{
		removedFrom.add(element_Numeral);
		return this;
	}
	public AdvancedAction matchMovedWithinComparator()
	{
		movedWithin.add(element_Comparator);
		return this;
	}
	public AdvancedAction matchMovedWithinOperator()
	{
		movedWithin.add(element_Operator);
		return this;
	}
	public AdvancedAction matchMovedWithinNumeral()
	{
		movedWithin.add(element_Numeral);
		return this;
	}
	

	public boolean isActionPerformedOnlyOnComparators()
	{
		return testAllMatchOnAllLists(e -> Objects.equals(e, element_Comparator));
	}
	public boolean isActionPerformedOnlyOnNumerals()
	{
		return testAllMatchOnAllLists(e -> Objects.equals(e, element_Numeral));
	}
	public boolean isActionPerformedOnlyOnOperators()
	{
		return testAllMatchOnAllLists(e -> Objects.equals(e, element_Operator));
	}
	private boolean testAllMatchOnAllLists(Predicate<String> requirements)
	{
		boolean a = addedTo.stream().allMatch(requirements);
		boolean r = removedFrom.stream().allMatch(requirements);
		boolean m = movedWithin.stream().allMatch(requirements);
		return a && r && m;
	}
	
	public boolean isActionPerformedOnNumeralsAsWell()
	{
		return testAnyMatchOnAllLists(e -> Objects.equals(e, element_Numeral));
	}
	public boolean isActionPerformedOnOperatorsAsWell()
	{
		return testAnyMatchOnAllLists(e -> Objects.equals(e, element_Operator));
	}
	private boolean testAnyMatchOnAllLists(Predicate<String> requirements)
	{
		boolean a = addedTo.stream().anyMatch(requirements);
		boolean r = removedFrom.stream().anyMatch(requirements);
		boolean m = movedWithin.stream().anyMatch(requirements);
		return a || r || m;
	}
	
	
	public boolean isMove(int i) 
	{
		return action.getAdded() == i && action.getRemoved() == i;
	}
	public boolean isMove1()
	{
		return isMove(1);
	}
	public boolean isMove2()
	{
		return isMove(2);
	}

}
