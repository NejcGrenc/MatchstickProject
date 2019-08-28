package grenc.masters.servlets;

import grenc.masters.servlets.bean.base.BaseServlet;
import grenc.simpleton.Beans;


public class DoneServlet extends BaseServlet
{	
	private static final long serialVersionUID = 8612184426160703877L;

	public DoneServlet()
	{
		super(Beans.get(DoneServletBean.class));
	}	
}
