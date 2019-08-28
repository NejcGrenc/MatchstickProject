package grenc.masters.servlets;

import grenc.masters.servlets.bean.base.BaseServlet;
import grenc.simpleton.Beans;


public class SelectTaskMatchstickServlet extends BaseServlet
{	
	private static final long serialVersionUID = 8612184426160703877L;

	public SelectTaskMatchstickServlet()
	{
		super(Beans.get(SelectTaskMatchstickServletBean.class));
	}	
}
