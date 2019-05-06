package grenc.masters.servlets.selector;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import grenc.simpleton.Beans;


public class DispatcherServlet extends HttpServlet
{
	private static final long serialVersionUID = 3495905445776786937L;

	private DispatcherServletBean dispatcherServletBean;
	
	public DispatcherServlet()
	{
		dispatcherServletBean = Beans.get(DispatcherServletBean.class);
	}	
	
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
		dispatcherServletBean.process(request, response, getServletContext());
	}
}
