package grenc.masters.servlets;

import grenc.masters.servlets.base.bean.BaseServlet;
import grenc.simpleton.Beans;


public class TaskWrapupServlet extends BaseServlet
{	
	private static final long serialVersionUID = 8650497030148112958L;

	public TaskWrapupServlet()
	{
		super(Beans.get(TaskWrapupServletBean.class));
	}	
}
	
