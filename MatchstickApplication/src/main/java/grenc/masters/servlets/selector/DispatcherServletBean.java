package grenc.masters.servlets.selector;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import grenc.masters.servlets.bean.base.ServletBean;
import grenc.masters.servlets.bean.service.ServletBeanProcessor;
import grenc.masters.servlets.developtools.InitialCallHandler;
import grenc.simpleton.annotation.Bean;
import grenc.simpleton.annotation.InsertBean;


@Bean
public class DispatcherServletBean
{
	@InsertBean
	private ServletBeanProcessor servletBeanProcessor;
	
	public void process(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext) throws IOException, ServletException
	{
		// New call
		System.out.println();
		
		mapParametersAsAttributes(request);
		
		// Process client's previous response phase
		String previousUrl = getPreviousUrl(request);
		ServletBean previousServlet = servletBeanProcessor.servletBeanByUrl(previousUrl);
		
		System.out.println("Processing client's response for: " + previousUrl);
		if (previousServlet != null)
			previousServlet.processClientsResponse(request, servletContext);
		
		
		if (InitialCallHandler.isInitial(request))
			new InitialCallHandler(request, response).handle();
		

		Selector selector = createNewSelector(request);
		String forwardUrl = selector.select();
			
		System.out.println("Forwarding request to: " + forwardUrl);
		RequestDispatcher dispatcher = buildDispatcher(forwardUrl, servletContext);
		dispatcher.forward(request, response);
	}
	
	private void mapParametersAsAttributes(HttpServletRequest request) 
	{
		List<String> params = Collections.list(request.getParameterNames());
		for (String param : params)
			request.setAttribute(param, request.getParameter(param));
	}
	
	private String getPreviousUrl(HttpServletRequest request)
	{
		String previous = (String) request.getAttribute("previousUrl");
		if (previous == null || previous.isEmpty())
			return null;
		return previous;
	}
	
	private Selector createNewSelector(HttpServletRequest request)
	{
		String forwardUrl = (String) request.getAttribute("forwardUrl");
		String previousUrl = (String) request.getAttribute("previousUrl");
		String sessionTag = (String) request.getAttribute("session");
		return new Selector()
				.withForwardUrl(forwardUrl)
				.withPreviousUrl(previousUrl)
				.withSessionTag(sessionTag);
	}
	
	public RequestDispatcher buildDispatcher(String forwardUrl, ServletContext servletContext)
	{
		return servletContext.getRequestDispatcher(forwardUrl);
	}
}
