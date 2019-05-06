package grenc.masters.servlets;

import grenc.masters.servlets.bean.base.BaseServlet;
import grenc.simpleton.Beans;


public class DataPresentServlet extends BaseServlet
{
	private static final long serialVersionUID = -8728770142383692863L;
	
	public DataPresentServlet()
	{
		super(Beans.get(DataPresentServletBean.class));
	}	
}
