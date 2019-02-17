package grenc.masters.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import grenc.masters.database.dao.SessionDAO;
import grenc.masters.database.entities.Session;
import grenc.masters.resources.PageElement;
import grenc.masters.resources.Script;
import grenc.masters.resources.Style;
import grenc.masters.servlets.base.BasePageServlet;
import grenc.masters.servlets.base.Servlet;
import grenc.masters.webpage.CreditsBall;
import grenc.masters.webpage.builder.AccountBallBuilder;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.masters.webpage.common.DataPresentBall;
import grenc.masters.webpage.common.LanguageBall;
import grenc.masters.webpage.common.Translate;

public class SelectTaskServlet extends BasePageServlet
{
	private static final long serialVersionUID = -8728770182383692863L;

	private SessionDAO sessionDAO;
	
	public SelectTaskServlet()
	{
		this.sessionDAO = SessionDAO.getInstance();
	}
	
	@Override
	public Servlet commonInstance()
	{
		return Servlet.SelectTaskServlet;
	}
	
	@Override
	protected void createWebPage(WebpageBuilder builder, HttpServletRequest request)
	{
		builder.setTitle("Experiments - Select task");

		builder.addStyle(Style.split_page);
		builder.addScript(Script.send);
		builder.appendPageElementFile(PageElement.select_task);

		Session session = sessionDAO.findSessionByTag((String) request.getAttribute("session"));
		new LanguageBall(builder, session.getLang(), commonInstance().getUrl()).set();
		new Translate(builder).translate("selecttask_txt1", "selecttask_txt2");
		new AccountBallBuilder().fromSession(session).withBuilder(builder).build().set();
		new DataPresentBall(builder, session).set();

		new CreditsBall(builder).set();
	}
	
	
	@Override
	public void processClientsResponse(HttpServletRequest request) throws IOException, ServletException
	{
		// No work to be done here
	}

}
