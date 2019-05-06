package grenc.masters.servlets;

import grenc.masters.servlets.bean.base.BaseServlet;
import grenc.simpleton.Beans;


public class LoginServlet extends BaseServlet
{	
	private static final long serialVersionUID = 7574509814157386246L;

	public LoginServlet()
	{
		super(Beans.get(LoginServletBean.class));
	}	
}
