package grenc.simpleton.utils;

import grenc.simpleton.Beans;

public class SimpleLogger
{
	public static void printAllBeans()
	{
		printLine("Existing Bean types:");
		for (Class<?> c : Beans.allRegisteredTypes())
			printLine("--- " + c);
	}
	
	public static void printError(Throwable e)
	{
		printLine("ERROR [" + e.getClass() + "]: "+ e.getMessage());
	}
	
	private static void printLine(String line)
	{
		System.out.println("[Simpleton] " + line);
	}
}
