package grenc.masters.servlets.base;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class BaseServlet extends HttpServlet implements ServletDesign
{
	private static final long serialVersionUID = 3803103574324745659L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		System.out.println("Servlet " + commonInstance().getName() + " received Post");
		processRequest(request, response);
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		System.out.println("Servlet " + commonInstance().getName() + " received Get");
		System.out.println("Get calls must be prohibited");
		processRequest(request, response);
	}
}
