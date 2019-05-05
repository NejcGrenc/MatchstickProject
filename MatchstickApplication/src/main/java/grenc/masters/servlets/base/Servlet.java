package grenc.masters.servlets.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import grenc.masters.servlets.CreditsServlet;
import grenc.masters.servlets.DataPresentServlet;
import grenc.masters.servlets.ImagesTaskServlet;
import grenc.masters.servlets.LanguageServlet;
import grenc.masters.servlets.LoginServlet;
import grenc.masters.servlets.MatchstickTaskLearnServlet;
import grenc.masters.servlets.MatchstickTaskObserveServlet;
import grenc.masters.servlets.MatchstickTaskServlet;
import grenc.masters.servlets.SelectTaskServlet;
import grenc.masters.servlets.TaskWrapupServlet;
import grenc.masters.servlets.UserDataServlet;
import grenc.masters.servlets.base.BaseServlet;


public class Servlet
{
	private static final LanguageServlet 			instanceLanguageServlet = new LanguageServlet();
	private static final LoginServlet 				instanceLoginServlet = new LoginServlet();
	private static final UserDataServlet 			instanceUserDataServlet = new UserDataServlet();
	private static final SelectTaskServlet 			instanceSelectTaskServlet = new SelectTaskServlet();
	private static final MatchstickTaskLearnServlet instanceMatchstickTaskLearnServlet = new MatchstickTaskLearnServlet();
	private static final MatchstickTaskObserveServlet instanceMatchstickTaskObserveServlet = new MatchstickTaskObserveServlet();
	private static final MatchstickTaskServlet 		instanceMatchstickTaskServlet = new MatchstickTaskServlet();
	private static final ImagesTaskServlet 			instanceImagesTaskServlet = new ImagesTaskServlet();
	private static final DataPresentServlet 		instanceDataPresenStervlet = new DataPresentServlet();
	private static final CreditsServlet 			instanceCreditsStervlet = new CreditsServlet();
	private static final TaskWrapupServlet 			instanceTaskWrapupServlet = new TaskWrapupServlet();
	
	public static Servlet LanguageServlet 			= new Servlet("/selectLanguage", 	"LanguageServlet", instanceLanguageServlet);
	public static Servlet LoginServlet 				= new Servlet("/login", 			"LoginServlet", instanceLoginServlet);
	public static Servlet UserDataServlet 			= new Servlet("/userData", 			"UserDataServlet", instanceUserDataServlet);
	public static Servlet SelectTaskServlet 		= new Servlet("/selectTask", 		"SelectTaskServlet", instanceSelectTaskServlet);
	public static Servlet MatchstickTaskLearnServlet = new Servlet("/matchstickTaskLearn", "MatchstickTaskLearnServlet", instanceMatchstickTaskLearnServlet);
	public static Servlet MatchstickTaskObserveServlet = new Servlet("/matchstickTaskObserve", "MatchstickTaskObserveServlet", instanceMatchstickTaskObserveServlet);
	public static Servlet MatchstickTaskServlet 	= new Servlet("/matchstickTask", 	"MatchstickTaskServlet", instanceMatchstickTaskServlet);
	public static Servlet ImagesTaskServlet 		= new Servlet("/imagesTask", 		"ImagesTaskServlet", instanceImagesTaskServlet);
	public static Servlet DataPresentServlet 		= new Servlet("/presentTaskData", 	"DataPresentervlet", instanceDataPresenStervlet);
	public static Servlet CreditsServlet			= new Servlet("/credits", 			"CreditsServlet", instanceCreditsStervlet);
	public static Servlet TaskWrapupServlet			= new Servlet("/taskWrapup",		"TaskWrapupServlet", instanceTaskWrapupServlet);
	
	private static List<Servlet> allServlets = new ArrayList<>(
		    Arrays.asList(
		    		LanguageServlet,
		    		LoginServlet,
		    		UserDataServlet,
		    		SelectTaskServlet,
		    		MatchstickTaskLearnServlet,
		    		MatchstickTaskObserveServlet,
		    		MatchstickTaskServlet,
		    		ImagesTaskServlet,
		    		DataPresentServlet,
		    		CreditsServlet,
		    		TaskWrapupServlet
		    		)); 
	
	
	private String url;
	private String name;
	private BaseServlet actualServletInstance;
	
	private Servlet (String url, String name, BaseServlet servletInstance)
	{
		this.url = url;
		this.name = name;
		this.actualServletInstance = servletInstance;
	}
	
	private Servlet (String url, String name, grenc.masters.servlets.base.bean.BaseServlet servletInstance)
	{
		this.url = url;
		this.name = name;
//		this.actualServletInstance = servletInstance;
	}
	
	public String getUrl()
	{
		return url;
	}
	
	public String getName()
	{
		return name;
	}
	
	public BaseServlet getServletInstance()
	{
		return actualServletInstance;
	}
	
	public static BaseServlet getServletInstanceForUrl(String url)
	{
		Servlet servletDesscription = getServletDescriptionForUrl(url);
		return (servletDesscription != null) ? servletDesscription.actualServletInstance : null;
	}
	public static Servlet getServletDescriptionForUrl(String url)
	{
		if (url == null || url.isEmpty())
			return null;
		
		String fixedUrl = fixUrl(url);
		for (Servlet servlet : allServlets)
		{
			if (servlet.getUrl().equals(fixedUrl))
				return servlet;
		}
		return null;
	}
	
	
	private static String fixUrl(String url)
	{
		String fixedUrl = url;
		if (! url.startsWith("/"))
		{
			fixedUrl = "/" + url;
		}
		return fixedUrl;
	}
	
}
