package grenc.masters.servlets.base;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import grenc.masters.webpage.builder.WebpageBuilder;

public abstract class BasePageServlet extends BaseServlet
{
	private static final long serialVersionUID = -2615973468843392056L;

	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException
	{
		response.setContentType("text/html;charset=UTF-8");
		try (PrintWriter out = response.getWriter())
		{
			String basePath = getServletContext().getRealPath("/");
			WebpageBuilder builder = new WebpageBuilder(basePath);
			includeUserSession(builder, request);
			includeCurrentCall(builder);
			createWebPage(builder, request);
			
			builder.writePage(out);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		System.out.println("Creaded page");	
	}
	
	private void includeUserSession(WebpageBuilder builder, HttpServletRequest request)
	{
		String session = (String) request.getAttribute("session");
		builder.appendHeadScriptCommand("var userSession = '" + session + "';");
	}

	private void includeCurrentCall(WebpageBuilder builder)
	{
		builder.appendHeadScriptCommand("var currUrl = '" + commonInstance().getUrl() + "';");
	}
	
	protected abstract void createWebPage(WebpageBuilder builder, HttpServletRequest request);
}
