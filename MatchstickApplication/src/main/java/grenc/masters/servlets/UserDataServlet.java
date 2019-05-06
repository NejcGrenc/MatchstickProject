package grenc.masters.servlets;

import grenc.masters.servlets.base.bean.BaseServlet;
import grenc.simpleton.Beans;


public class UserDataServlet extends BaseServlet
{	
	private static final long serialVersionUID = 1529076118091189351L;

	public UserDataServlet()
	{
		super(Beans.get(UserDataServletBean.class));
	}	
}
