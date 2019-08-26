package grenc.masters.servlets.bean.base;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import grenc.masters.Encoding;
import grenc.masters.servlets.developtools.RefreshCache;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.simpleton.Beans;

public abstract class BasePageServlet implements ServletBean
{
	private RefreshCache refreshCache;
	
	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext)
			throws IOException, ServletException
	{
        request.setCharacterEncoding(Encoding.common);
		response.setCharacterEncoding(Encoding.common);
		try (PrintWriter out = response.getWriter())
		{
			String basePath = servletContext.getRealPath("/");
			WebpageBuilder builder = new WebpageBuilder(basePath);
			includeUserSession(builder, request);
			includeCurrentCall(builder);
			createWebPage(builder, request, servletContext);
			
			builder.writePage(out);
			
			cacheResponse(request, builder);
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
		builder.appendHeadScriptCommand("var currUrl = '" + url() + "';");
	}
	
	private void cacheResponse(HttpServletRequest request, WebpageBuilder responseBuilder)
	{
		// Lazy load
		if (refreshCache == null)
			refreshCache = Beans.get(RefreshCache.class);
		refreshCache.cacheResponse(request, responseBuilder);
	}
	
	protected abstract void createWebPage(WebpageBuilder builder, HttpServletRequest request, ServletContext servletContext);
}
