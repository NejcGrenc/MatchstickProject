package grenc.masters.utils;

import java.util.List;
import java.util.Map;

import grenc.masters.database.entities.Session;
import grenc.masters.servlets.bean.base.BasePageServlet;

public class Logger
{
	private String className;
	
	public Logger(String className)
	{
		this.className = className;
	}
	public Logger(BasePageServlet servletBean)
	{
		this.className = servletBean.url();
	}
	
	
	public static void sLog()
	{
		mainLog(general(), "");
	}

	public static void sLog(Session session)
	{
		mainLog(session(session), "");
	}

	public static void sLog(String text)
	{
		mainLog(general(), text);
	}

	public static void sLog(Session session, String text)
	{
		mainLog(session(session), text);
	}
	
	public void log()
	{
		mainLog(general(), className(), "");
	}
	
	public void log(String text)
	{
		mainLog(general(), className(), text);
	}

	public void log(Session session)
	{
		mainLog(session(session), className(), "");
	}
	
	public void log(Session session, String text)
	{
		mainLog(session(session), className(), text);
	}
	
	public <T> void printList(Session session, String title, List<T> list) 
	{
		mainLog(session(session), className(), "List " + title + " contains elements:");
		printList(session, list);
	}
	
	public <T> void printList(Session session, List<T> list) 
	{
		for (T el : list)
		{
			mainLog(session(session), className(), " -  " + el);
		}
	}
	
	public <T, B> void printMap(Session session, Map<T, B> map) 
	{
		for (Map.Entry<T, B> el : map.entrySet())
		{
			mainLog(session(session), className(), " -  [" + el.getKey() + "] " + el.getValue());
		}
	}
	

	private static String general()
	{
		return "[ General      ]";
	}

	private static String session(Session session)
	{
		if (session == null)
			return "[Session =   ? ]";
		return String.format("[Session = %4d]", session.getId());
	}
	
	private String className()
	{
		return String.format("[%-24s]", className);
	}
	
	private static void mainLog(String... elements)
	{
		System.out.println(String.join(" ", elements));
	}

}
