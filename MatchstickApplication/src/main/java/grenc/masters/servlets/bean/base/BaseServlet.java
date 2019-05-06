package grenc.masters.servlets.bean.base;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public abstract class BaseServlet extends HttpServlet
{
	private static final long serialVersionUID = 3803103574325745659L;

	protected ServletBean servletBeanInstance;
	
	protected BaseServlet(ServletBean instance)
	{
		this.servletBeanInstance = instance;
	}
	
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		System.out.println("Servlet " + servletBeanInstance.getClass().getName() + " received Post");
		servletBeanInstance.processRequest(request, response, getServletContext());
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		System.out.println("Servlet " + servletBeanInstance.getClass().getName() + " received Get");
		System.out.println("Get calls must be prohibited");
		servletBeanInstance.processRequest(request, response, getServletContext());
	}
}
