package grenc.masters.servlets.selector;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import grenc.masters.Encoding;
import grenc.masters.database.dao.SessionDAO;
import grenc.masters.database.entities.Session;
import grenc.masters.servlets.bean.base.ServletBean;
import grenc.masters.servlets.bean.service.ServletBeanProcessor;
import grenc.masters.servlets.developtools.InitialCallHandler;
import grenc.masters.servlets.developtools.RefreshCache;
import grenc.masters.utils.Logger;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.simpleton.annotation.Bean;
import grenc.simpleton.annotation.InsertBean;


@Bean
public class DispatcherServletBean
{
	@InsertBean
	private RefreshCache refreshCache;
	@InsertBean
	private ServletBeanProcessor servletBeanProcessor;
	@InsertBean
	private InitialCallHandler initialCallHandler;
	@InsertBean
	private Selector selector;
	
	@InsertBean
	private SessionDAO sessionDao;
	
	private Logger logger = new Logger(DispatcherServletBean.class.getSimpleName());

	
	public DispatcherServletBean() {};
	
	public DispatcherServletBean(ServletBeanProcessor servletBeanProcessor)
	{
		this.servletBeanProcessor = servletBeanProcessor;
	}
	
	
	public void process(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext) throws IOException, ServletException
	{
		Session session = null;
		try 
		{
			String sessionTag = (String) request.getAttribute("session");
			if (sessionTag != null)
				session = sessionDao.findSessionByTag(sessionTag);
			
			// New call
			logger.log(session);
			
			mapParametersAsAttributes(request);
			
			// In case a page refresh occurred, return cached data
			refreshCache.deleteTimeoutedData();
			if (refreshCache.isEligibleForRefresh(request))
			{
				logger.log(session, "New request has been determined to be a page refresh. Returning previous cached data.");
				outputExistingResponse(response, refreshCache.getCachedResponse(request));
				return;
			}
			
			// Process client's previous response phase
			String previousUrl = getPreviousUrl(request);
			ServletBean previousServlet = servletBeanProcessor.servletBeanByUrl(previousUrl);
			
			logger.log(session, "Processing client's response for: " + previousUrl);
			if (previousServlet != null)
				previousServlet.processClientsResponse(request, servletContext);
			
			
			if (initialCallHandler.isInitial(request))
				initialCallHandler.handle(request, response);
			
			String forwardUrl = selectNewForwardUrl(request, response);
				
			logger.log(session, "Forwarding request to: " + forwardUrl);
			RequestDispatcher dispatcher = buildDispatcher(forwardUrl, servletContext);
			dispatcher.forward(request, response);
			
		}
		catch (Throwable ex)
		{
			logger.log(session);
			logger.log(session, "Exception occoured: " + ex.getMessage());
			logger.log(session);

			logger.log(session, "Forwarding request to: /error");
			RequestDispatcher dispatcher = buildDispatcher("/error", servletContext);
			dispatcher.forward(request, response);
		}
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
	
	private String selectNewForwardUrl(HttpServletRequest request, HttpServletResponse response)
	{
		String forwardUrl = (String) request.getAttribute("forwardUrl");
		String previousUrl = (String) request.getAttribute("previousUrl");
		String sessionTag = (String) request.getAttribute("session");
		
		String newForwardUrl = selector.select(forwardUrl, previousUrl, sessionTag);
		if (newForwardUrl == "UNKNOWN")
		{
			request.setAttribute("restart_anew", true);
			initialCallHandler.handle(request, response);
			return selector.select(forwardUrl, previousUrl, sessionTag);
		}
		return newForwardUrl;
	}
	
	public RequestDispatcher buildDispatcher(String forwardUrl, ServletContext servletContext)
	{
		return servletContext.getRequestDispatcher(forwardUrl);
	}
	
	private void outputExistingResponse(HttpServletResponse response, WebpageBuilder cachedResponse) throws IOException
	{
		response.setCharacterEncoding(Encoding.common);
		cachedResponse.writePage(response.getWriter());
	}
}
