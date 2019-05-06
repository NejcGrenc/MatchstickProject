package grenc.masters.servlets;

import grenc.masters.servlets.bean.base.BaseServlet;
import grenc.simpleton.Beans;


public class ImagesTaskServlet extends BaseServlet
{	
	private static final long serialVersionUID = -1953103899973255388L;

	public ImagesTaskServlet()
	{
		super(Beans.get(ImagesTaskServletBean.class));
	}	
}
