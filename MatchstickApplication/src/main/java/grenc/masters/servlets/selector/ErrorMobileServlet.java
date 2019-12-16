package grenc.masters.servlets.selector;

import grenc.masters.servlets.bean.base.BaseServlet;
import grenc.simpleton.Beans;


public class ErrorMobileServlet extends BaseServlet
{
	private static final long serialVersionUID = -6643916934363225446L;
	
	public ErrorMobileServlet()
	{
		super(Beans.get(ErrorMobileServletBean.class));
	}	
}	
