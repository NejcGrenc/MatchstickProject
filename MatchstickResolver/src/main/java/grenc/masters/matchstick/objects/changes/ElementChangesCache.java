package grenc.masters.matchstick.objects.changes;

import java.util.HashMap;
import java.util.Set;

import grenc.masters.matchstick.objects.main.Action;
import grenc.masters.matchstick.objects.main.Element;

public class ElementChangesCache
{
	private HashMap<Element, HashMap<Action, Set<Element>>> cacheMap;
	
	// Sole instance
	private static ElementChangesCache cache = new ElementChangesCache ();
	
	private ElementChangesCache ()
	{
		cacheMap = new HashMap<>();
	}
	
	private HashMap<Element, HashMap<Action, Set<Element>>> getMap()
	{
		return cacheMap;
	}
	
	public static Set<Element> attemptToGet(Element original, int added, int removed)
	{
		return attemptToGet(original, new Action(added, removed));
	}
	public static Set<Element> attemptToGet(Element original, Action action)
	{
		HashMap<Action, Set<Element>> elementMap = cache.getMap().get(original);
		if (elementMap == null) 
			return null;		
		return elementMap.get(action);
	}
	
	public static void put(Element original, int added, int removed, Set<Element> changed)
	{
		put(original, new Action(added, removed), changed);
	}
	public static void put(Element original, Action action, Set<Element> changed)
	{
		if (! cache.getMap().containsKey(original))
			cache.getMap().put(original, new HashMap<>());
		cache.getMap().get(original).put(action, changed);
	}
}
