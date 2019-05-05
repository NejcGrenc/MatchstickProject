package grenc.masters.servlets.base.bean;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ServletBean
{	
	String url();
	
	void processRequest(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext)
			throws IOException, ServletException;
	
	void processClientsResponse(HttpServletRequest request, ServletContext servletContext)
			throws IOException, ServletException;
}
