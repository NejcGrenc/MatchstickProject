package grenc.masters.utils;

import java.util.List;
import java.util.Map;

public class PrintUtils
{

	public static <T> void printList(String title, List<T> list) 
	{
		System.out.println("List " + title + " contains elements:");
		printList(list);
	}
	
	public static <T> void printList(List<T> list) 
	{
		for (T el : list)
		{
			System.out.println(" -  " + el);
		}
	}
	
	public static <T, B> void printMap(Map<T, B> map) 
	{
		for (Map.Entry<T, B> el : map.entrySet())
		{
			System.out.println(" -  [" + el.getKey() + "] " + el.getValue());
		}
	}
	
}
