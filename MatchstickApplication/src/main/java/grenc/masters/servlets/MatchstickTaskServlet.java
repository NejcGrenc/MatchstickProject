package grenc.masters.servlets;

import grenc.masters.servlets.base.bean.BaseServlet;
import grenc.simpleton.Beans;


public class MatchstickTaskServlet extends BaseServlet
{	
	private static final long serialVersionUID = 7897105970709246285L;

	public MatchstickTaskServlet()
	{
		super(Beans.get(MatchstickTaskServletBean.class));
	}	
}
