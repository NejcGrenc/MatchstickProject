package grenc.masters.webpage.builder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import grenc.masters.resources.PageElement;
import grenc.masters.resources.Script;
import grenc.masters.resources.Style;


public class WebpageBuilder
{
	private String basePath;
	private String title;
	private HeadBuilder head;
	private BodyBuilder body;
	
	public WebpageBuilder(String basePath)
	{
		this.basePath = basePath;
		this.head = new HeadBuilder();
		this.body = new BodyBuilder();
	}
	
	public void writePage(PrintWriter out)
	{
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		head.write(out);
		body.write(out);
		out.println("</html>");
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	@Deprecated
	public void addStyle(String styleName)
	{
		head.addStyle(styleName);
	}
	public void addStyle(Style style)
	{
		head.addStyle(style.path());
	}
	
	@Deprecated
	public void addScript(String scriptName)
	{
		head.addScript(scriptName);
	}
	public void addScript(Script script)
	{
		head.addScript(script.path());
	}
	
	@Deprecated
	public void appendPageElementFile(String pageName)
	{
		body.addElementPage(pageName);
	}
	public void appendPageElementFile(PageElement page)
	{
		body.addElementPage(page.path());
		for (Style style : page.getDependentStyles())
			addStyle(style);
		for (Script script : page.getDependentScripts())
			addScript(script);
	}
	
	public void appendPageElement(String element)
	{
		body.addElement(element);
	}
	
	public void setBodyStyle(String styleClass)
	{
		body.appendBodyStyleClass(styleClass);
	}
	
	public void appendHeadScriptCommand(String command)
	{
		head.appendToHeaderScript(command);
	}
	
	public void appendBodyScriptCommand(String command)
	{
		body.appendToBodyScript(command);
	}
	
	
	
	/**
	 * HEAD
	 */		
	private class HeadBuilder 
	{	
		private ArrayList<String> styles;
		private ArrayList<String> scripts;
		private String headerScript;
		
		HeadBuilder()
		{
			styles = new ArrayList<>();
			scripts = new ArrayList<>();
			headerScript = "\n";
		}
		
		public void write(PrintWriter out)
		{
			out.println("<head>");
			out.println("<meta charset=\"UTF-8\">");
			out.println("<title>" + title + "</title>");
			for (String style : styles)
				out.println(writeStyle(style));
			for (String script : scripts)
				out.println(writeScript(script));
			out.println(writeHeaderScript());
			out.println("</head>");
		}
		
		void addStyle(String styleName)
		{
			if (! styles.contains(styleName))
				styles.add(styleName);
		}
		String writeStyle(String styleName)
		{
			return "<link rel='stylesheet' type='text/css' href='" + styleName + "'>";
		}
		
		void addScript(String scriptName)
		{
			if (! scripts.contains(scriptName))
				scripts.add(scriptName);
		}
		String writeScript(String scriptName)
		{
			return "<script src='" + scriptName +"' type='text/javascript'></script>";
		}
		
		void appendToHeaderScript(String command)
		{
			headerScript += command + "\n";
		}	
		String writeHeaderScript()
		{
			return "<script>" + headerScript + "</script>";
		}
	}
	
	
	
	/**
	 * BODY
	 */			
	private class BodyBuilder 
	{
		private String bodyStyleClass;
		private ArrayList<PageElement> pageElements;
		private String bodyScript;
		
		private class PageElement
		{
			boolean isPageName;
			String content;
		}
		
		BodyBuilder()
		{
			bodyStyleClass = "";
			pageElements = new ArrayList<>();
			bodyScript = "\n";
		}
		
		public void write(PrintWriter out)
		{
			out.println("<body class='" + bodyStyleClass + "'>");
			for (PageElement pageElement : pageElements)
			{
				if (pageElement.isPageName)
				{
					outputFile(out, pageElement.content);
				}
				else
				{
					out.println(pageElement.content);
				}
			}
			out.println(writeBodyScript());
			out.println("</body>");
		}
		
		private void outputFile(PrintWriter out, String file)
		{
			try 
			{
			    BufferedReader in = new BufferedReader(new FileReader(basePath + file));
			    String str;
			    while ((str = in.readLine()) != null) 
			    {
			    	out.println(str);
			    }
			    in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		void appendBodyStyleClass(String clazz)
		{
			bodyStyleClass += clazz;
		}
		
		void addElementPage(String pageName)
		{
			PageElement pe = new PageElement();
			pe.isPageName = true;
			pe.content = pageName;
			pageElements.add(pe);
		}
		void addElement(String element)
		{
			PageElement pe = new PageElement();
			pe.isPageName = false;
			pe.content = element;
			pageElements.add(pe);
		}
		
		void appendToBodyScript(String command)
		{
			bodyScript += command + "\n";
		}	
		String writeBodyScript()
		{
			return "<script>" + bodyScript + "</script>";
		}
	}
	
}
