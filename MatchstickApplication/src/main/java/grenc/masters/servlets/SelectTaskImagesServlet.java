package grenc.masters.servlets;

import grenc.masters.servlets.bean.base.BaseServlet;
import grenc.simpleton.Beans;


public class SelectTaskImagesServlet extends BaseServlet
{	
	private static final long serialVersionUID = 8612184426160703877L;

	public SelectTaskImagesServlet()
	{
		super(Beans.get(SelectTaskImagesServletBean.class));
	}	
}
