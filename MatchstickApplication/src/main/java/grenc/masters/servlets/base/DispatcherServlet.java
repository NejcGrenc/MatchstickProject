package grenc.masters.servlets.base;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import grenc.masters.servlets.developtools.InitialCallHandler;
import grenc.masters.servlets.developtools.SkipLogin;

public class DispatcherServlet extends HttpServlet
{
	private static final long serialVersionUID = 3495905445776786937L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		process(request, response);
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		process(request, response);
	}
	
	private void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		// New call
		System.out.println();
		
		mapParametersAsAttributes(request);
		
		// Process client's previous response phase
		String previousUrl = getPreviousUrl(request);
		BaseServlet previousServlet = Servlet.getServletInstanceForUrl(previousUrl);
		
		System.out.println("Processing client's response for: " + previousUrl);
		if (previousServlet != null)
			previousServlet.processClientsResponse(request);
		
		
		if (InitialCallHandler.isInitial(request))
			new InitialCallHandler(request).handle();
		

		Selector selector = createNewSelector(request);
		String forwardUrl = selector.select();
			
		System.out.println("Forwarding request to: " + forwardUrl);
		RequestDispatcher dispatcher = buildDispatcher(forwardUrl);
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
	
	public RequestDispatcher buildDispatcher(String forwardUrl)
	{
		return getServletContext().getRequestDispatcher(forwardUrl);
	}
}
