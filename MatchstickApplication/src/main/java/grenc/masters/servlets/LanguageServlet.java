package grenc.masters.servlets;

import grenc.masters.servlets.bean.base.BaseServlet;
import grenc.simpleton.Beans;


public class LanguageServlet extends BaseServlet
{	
	private static final long serialVersionUID = -8728770182383692863L;

	public LanguageServlet()
	{
		super(Beans.get(LanguageServletBean.class));
	}	
}
