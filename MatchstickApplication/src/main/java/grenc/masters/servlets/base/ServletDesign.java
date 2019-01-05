package grenc.masters.servlets.base;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ServletDesign
{
	Servlet commonInstance();
	
	void processRequest(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException;
	
	void processClientsResponse(HttpServletRequest request)
			throws IOException, ServletException;
	
}
