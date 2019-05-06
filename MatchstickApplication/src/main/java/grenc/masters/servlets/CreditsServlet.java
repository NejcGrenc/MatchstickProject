package grenc.masters.servlets;

import grenc.masters.servlets.base.bean.BaseServlet;
import grenc.simpleton.Beans;


public class CreditsServlet extends BaseServlet
{
	private static final long serialVersionUID = -6643916934363285446L;
	
	public CreditsServlet()
	{
		super(Beans.get(CreditsServletBean.class));
	}	
}	
