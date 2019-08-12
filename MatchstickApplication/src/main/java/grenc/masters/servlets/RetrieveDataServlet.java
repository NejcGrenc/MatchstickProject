package grenc.masters.servlets;

import grenc.masters.servlets.bean.base.BaseServlet;
import grenc.simpleton.Beans;


public class RetrieveDataServlet extends BaseServlet
{	
	private static final long serialVersionUID = -1953103899973255388L;

	public RetrieveDataServlet()
	{
		super(Beans.get(RetrieveDataServletBean.class));
	}	
}
