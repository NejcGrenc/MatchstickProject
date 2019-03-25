package grenc.masters.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import grenc.masters.database.dao.SessionDAO;
import grenc.masters.database.dao.SubjectDAO;
import grenc.masters.database.entities.Session;
import grenc.masters.database.entities.Subject;
import grenc.masters.resources.PageElement;
import grenc.masters.resources.Script;
import grenc.masters.resources.Style;
import grenc.masters.servlets.base.BasePageServlet;
import grenc.masters.servlets.base.Servlet;
import grenc.masters.webpage.builder.AccountBallBuilder;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.masters.webpage.common.DataPresentBall;
import grenc.masters.webpage.common.LanguageBall;
import grenc.masters.webpage.common.Translate;

public class UserDataServlet extends BasePageServlet
{
	private static final long serialVersionUID = 2368890582418928946L;

	private SessionDAO sessionDAO;
	private SubjectDAO subjectDAO;
	
	public UserDataServlet()
	{
		this.sessionDAO = SessionDAO.getInstance();
		this.subjectDAO = SubjectDAO.getInstance();
	}
	
	@Override
	public Servlet commonInstance()
	{
		return Servlet.UserDataServlet;
	}

	@Override
	protected void createWebPage(WebpageBuilder builder, HttpServletRequest request)
	{
		builder.setTitle("Experiments - Select language");
		
		builder.addStyle(Style.background);
		builder.setBodyStyle("background");
		
		builder.addStyle(Style.language_page);
		builder.addStyle(Style.buttons);
		builder.addStyle(Style.style);
		builder.addStyle(Style.input);	
		
		builder.addScript(Script.page_functions);
		builder.addScript(Script.send);

		Session session = sessionDAO.findSessionByTag((String) request.getAttribute("session"));
		new LanguageBall(builder, session.getLang(), commonInstance().getUrl()).set();
		new Translate(builder, Script.translate_userdata)
			.translateAll()
			.translateSpecial("m_userdata_input_age", "placeholder");
		new AccountBallBuilder().fromSession(session).withBuilder(builder).build(getServletContext()).set();
		new DataPresentBall(builder, session).set();

		builder.appendPageElementFile(PageElement.user_data);
	}
	
	@Override
	public void processClientsResponse(HttpServletRequest request) throws IOException, ServletException
	{
		provideDataToSubject(request);
	}

	private void provideDataToSubject(HttpServletRequest request)
	{
		String sessionTag = (String) request.getAttribute("session");
		
		String ageStr = (String) request.getAttribute("userdata_age");
		Integer age = (ageStr != null) ? Integer.parseInt(ageStr) : null;
		String sex = (String) request.getAttribute("userdata_sex");
		String lang = (String) request.getAttribute("lang");
		
		System.out.println("Upsert");
		System.out.println(" - for session: " + sessionTag);
		System.out.println(" - update subject data: {" + age + ", " + sex + ", " + lang + "}");

		Session session = sessionDAO.findSessionByTag(sessionTag);
		Subject subject = subjectDAO.findSubjectById(session.getSubjectId());
		subjectDAO.updateSubject(subject.getId(), age, sex, lang);
	}
	
	
}
