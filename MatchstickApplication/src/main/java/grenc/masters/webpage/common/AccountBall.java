package grenc.masters.webpage.common;

import javax.servlet.ServletContext;

import grenc.masters.database.dao.SubjectDAO;
import grenc.masters.database.entities.Session;
import grenc.masters.database.entities.Subject;
import grenc.masters.resources.PageElement;
import grenc.masters.resources.Style;
import grenc.masters.webpage.builder.PopupBuilderAbstract;
import grenc.masters.webpage.builder.WebpageBuilder;


public class AccountBall extends PopupBuilderAbstract
{
	private WebpageBuilder builder;
	private Subject subject;
	private ServletContext servletContext;
	
	public AccountBall(WebpageBuilder builder, Subject subject, ServletContext servletContext)
	{
		this.builder = builder;
		this.subject = subject;
		this.servletContext = servletContext;
	}
	
	public AccountBall(WebpageBuilder builder, Session session, ServletContext servletContext)
	{
		this.builder = builder;
		this.servletContext = servletContext;
		this.subject = SubjectDAO.getInstance().findSubjectById(session.getSubjectId());
	}

	public void set()
	{
		builder.addStyle(Style.buttons);
		builder.addStyle(Style.popup);
		builder.appendPageElementFile(PageElement.account_ball);
		attachPopup();
	}
	
	private void attachPopup() {
		String content = readFile(servletContext, PageElement.account_ball_popup.path());

		new Popup(builder, "popupName").setOpenButton("accountButton").setText(content).set();
		builder.appendBodyScriptCommand("setName('" + subject.getName() + "');");
	}
	
}
