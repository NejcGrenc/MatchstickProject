package grenc.masters.matchstick.objects.changes;

import java.util.HashSet;
import java.util.Set;

import grenc.masters.matchstick.confirm.valid.Validator;
import grenc.masters.matchstick.objects.main.Element;
import grenc.masters.matchstick.run.main.Settings;


public class ElementChanges 
{
	private Element original;
	private Set<Element> set;
	
	public ElementChanges (Element original)
	{
		this.original = original;
	}
	
	public Set<Element> findAllChanged(int added, int removed)
	{
		// Attempt to get from cache
		if (Settings.useElementCache)
		{
			Set<Element> retievedSet = ElementChangesCache.attemptToGet(original, added, removed);
			if (retievedSet != null)
				return retievedSet;
		}

		set = new HashSet<>();
		findAllChanged(original, added, removed);

		// Add to cache
		if (Settings.useElementCache)
			ElementChangesCache.put(original, added, removed, set);
		return set;
	}
	
	private void findAllChanged(Element current, int added, int removed)
	{
		if (added == 0 && removed == 0)
		{
			if (Validator.isValid(current))
			{
				set.add(current.copy());
			}
			return;
		}
		
		tryRemoveSingle(current, added, removed);
		tryAddSingle(current, added, removed);
	}
	
	private void tryRemoveSingle(Element current, int added, int removed)
	{
		if (removed == 0)
			return;
		
		int[] currDisplay = current.getDisplay();
		for (int i = 0; i < currDisplay.length; i++)
		{
			if (currDisplay[i] == 1)
			{
				currDisplay[i] = 0;
				findAllChanged(current, added, removed-1);
				currDisplay[i] = 1;
			}		
		}
	}
	private void tryAddSingle(Element current, int added, int removed)
	{
		if (added == 0)
			return;
		
		int[] currDisplay = current.getDisplay();
		for (int i = 0; i < currDisplay.length; i++)
		{
			if (currDisplay[i] == 0)
			{
				currDisplay[i] = 1;
				findAllChanged(current, added-1, removed);
				currDisplay[i] = 0;
			}		
		}
	}
	
}
