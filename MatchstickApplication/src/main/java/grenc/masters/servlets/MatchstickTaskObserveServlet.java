package grenc.masters.servlets;

import grenc.masters.servlets.bean.base.BaseServlet;
import grenc.simpleton.Beans;


public class MatchstickTaskObserveServlet extends BaseServlet
{	
	private static final long serialVersionUID = -8355344168216966568L;

	public MatchstickTaskObserveServlet()
	{
		super(Beans.get(MatchstickTaskObserveServletBean.class));
	}	
}	
