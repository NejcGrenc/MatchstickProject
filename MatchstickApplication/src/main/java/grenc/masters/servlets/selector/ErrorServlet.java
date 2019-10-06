package grenc.masters.servlets.selector;

import grenc.masters.servlets.bean.base.BaseServlet;
import grenc.simpleton.Beans;


public class ErrorServlet extends BaseServlet
{
	private static final long serialVersionUID = -6643916934363285446L;
	
	public ErrorServlet()
	{
		super(Beans.get(ErrorServletBean.class));
	}	
}	
