package grenc.masters.utils;

import java.util.List;

public class PrintUtils
{

	public static <T> void printList(String title, List<T> list) 
	{
		System.out.println("List " + title + " contains elements:");
		for (T el : list)
		{
			System.out.println(" -  " + el.toString());
		}
	}
	
}
