package grenc.masters.servlets;

import grenc.masters.servlets.base.bean.BaseServlet;
import grenc.simpleton.Beans;


public class MatchstickTaskLearnServlet extends BaseServlet
{	
	private static final long serialVersionUID = -1857196108702205299L;

	public MatchstickTaskLearnServlet()
	{
		super(Beans.get(MatchstickTaskLearnServletBean.class));
	}	
}	
