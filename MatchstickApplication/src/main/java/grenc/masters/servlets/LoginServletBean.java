package grenc.masters.servlets;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import grenc.masters.database.dao.SessionDAO;
import grenc.masters.database.dao.SubjectDAO;
import grenc.masters.database.entities.Session;
import grenc.masters.database.entities.Subject;
import grenc.masters.resources.PageElement;
import grenc.masters.resources.Script;
import grenc.masters.resources.Style;
import grenc.masters.servlets.bean.base.BasePageServlet;
import grenc.masters.servlets.delegate.popup.LoginAgreementPopup;
import grenc.masters.uservalidation.ValidateUserSession;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.masters.webpage.common.DataPresentBall;
import grenc.masters.webpage.common.LanguageBall;
import grenc.masters.webpage.common.Translate;
import grenc.simpleton.annotation.Bean;
import grenc.simpleton.annotation.InsertBean;


@Bean
public class LoginServletBean extends BasePageServlet
{
	@InsertBean
	private SessionDAO sessionDAO;
	@InsertBean
	private SubjectDAO subjectDAO;
	@InsertBean
	private ValidateUserSession validator;
	@InsertBean
	private LoginAgreementPopup loginAgreementPopup;

	@Override
	public String url()
	{
		return "/login";
	}

	@Override
	protected void createWebPage(WebpageBuilder builder, HttpServletRequest request, ServletContext servletContext)
	{
		builder.setTitle("Experiments - Login");
		
		builder.addStyle(Style.background);
		builder.setBodyStyle("background");
		
		builder.addStyle(Style.language_page);
		builder.addStyle(Style.buttons);
		builder.addStyle(Style.style);
		builder.addStyle(Style.input);	
		
		builder.addScript(Script.page_functions);
		builder.addScript(Script.send);

		
		Session session = sessionDAO.findSessionByTag((String) request.getAttribute("session"));
		
		new LanguageBall(builder, session.getLang(), url()).set();
		new Translate(builder, Script.translate_login)
			.translateAll()
			.translateSpecial("m_nameInput", "placeholder");

		new DataPresentBall(builder, session).set();
		loginAgreementPopup.createPopup(builder, servletContext, session.getLang());
		
						
		builder.appendPageElementFile(PageElement.login);
	}
	
	@Override
	public void processClientsResponse(HttpServletRequest request, ServletContext servletContext) throws IOException, ServletException
	{
		String agreementAccepted = (String) request.getAttribute("agreement_accepted");
		if (agreementAccepted == null || agreementAccepted.isEmpty())
		{
			System.out.println("Did not login while on Login page! (probably clicked one of the up-right buttons)");
			return;
		}
		
		if (Boolean.valueOf(agreementAccepted))
		{
			System.out.println("User has accepted the agreement.");
			createLoginSubject(request);
		}
		else
			System.out.println("User has NOT accepted the agreement.");
	}

	private void createLoginSubject(HttpServletRequest request)
	{
		if (validator.isFreshIP(request))
		{
			Subject newSubject = createNewSubject(request);
			newSubject = validator.updateSubject(newSubject, request);
			subjectDAO.updateSubjectOriginal(newSubject.getId(), true);
		}
		else
		{
			Subject newSubject = createNewSubject(request);
			newSubject = validator.updateSubject(newSubject, request);
			
			// User is tainted (risk != 0)
			subjectDAO.updateSubjectOriginal(newSubject.getId(), false);
			String sessionTag = (String) request.getAttribute("session");
			increaseSessionRisk(sessionTag, 10);
		}
	}
	
	private Subject createNewSubject(HttpServletRequest request)
	{
		String sessionTag = (String) request.getAttribute("session");
		
		System.out.println("Create a new subject");
		System.out.println(" - for session: " + sessionTag);
		
		Session session = sessionDAO.findSessionByTag(sessionTag);
		Subject subject = subjectDAO.insertSubject(session.getId());
		
		sessionDAO.updateSessionSubjectId(session.getId(), subject.getId());
		
		return subject;
	}
	
	private void increaseSessionRisk(String sessionTag, int increasedRisk)
	{
		Session session = sessionDAO.findSessionByTag(sessionTag);
		int previousRisk = session.getRisk();
		int newRisk = previousRisk + increasedRisk;
		System.out.println("Upsert");
		System.out.println(" - for session: " + sessionTag);
		System.out.println(" - increasing risk to: " + newRisk);

		sessionDAO.updateSessionRisk(session.getId(), newRisk);
	}
	
}