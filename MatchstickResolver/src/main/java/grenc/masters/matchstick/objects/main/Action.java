package grenc.masters.matchstick.objects.main;

public class Action {

	private int added;
	private int removed;

	public Action (int moved)
	{
		this (moved, moved);
	}
	
	public Action (int added, int removed)
	{
		this.added = added;
		this.removed = removed;
	}
	
	public int getAdded()
	{
		return added;
	}
	public int getRemoved()
	{
		return removed;
	}
	
	
	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof Action)) return false;
		Action a = (Action) o;
		return this.added == a.added
				&& this.removed == a.removed;
	}
	
	@Override 
	public int hashCode()
	{
		return 7 * this.added + 31 * this.removed;
	}
	
	@Override
	public String toString()
	{
		return "(+" + this.added + ", -" + this.removed + ")"; 
	}
}
