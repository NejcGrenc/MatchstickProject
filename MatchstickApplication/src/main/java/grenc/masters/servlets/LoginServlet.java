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
import grenc.masters.uservalidation.ValidateUserSession;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.masters.webpage.common.DataPresentBall;
import grenc.masters.webpage.common.LanguageBall;
import grenc.masters.webpage.common.Popup;
import grenc.masters.webpage.common.Translate;

public class LoginServlet extends BasePageServlet
{
	private static final long serialVersionUID = 2368890582418928946L;

	private SessionDAO sessionDAO;
	private SubjectDAO subjectDAO;
	
	public LoginServlet()
	{
		this.sessionDAO = SessionDAO.getInstance();
		this.subjectDAO = SubjectDAO.getInstance();
	}
	
	@Override
	public Servlet commonInstance()
	{
		return Servlet.LoginServlet;
	}

	@Override
	protected void createWebPage(WebpageBuilder builder, HttpServletRequest request)
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
		

//		TODO : This code has associated JS code
//		
//		String subjectName = (String) request.getAttribute("subjectName");
//		if (isLoginWithExistingUser(subjectName))
//		{
//			builder.appendBodyScriptCommand("insertExistingName('" + subjectName + "');");
//			builder.appendBodyScriptCommand("askForRepeated();");
//		}
		
		Session session = sessionDAO.findSessionByTag((String) request.getAttribute("session"));
		
		new LanguageBall(builder, session.getLang(), commonInstance().getUrl()).set();
		new Translate(builder, Script.translate_login)
			.translateAll()
			.translateSpecial("m_nameInput", "placeholder");

		new DataPresentBall(builder, session).set();
		
		createTermsAndAgreementsPopup(builder);
				
		builder.appendPageElementFile(PageElement.login);
	}
	
	@Override
	public void processClientsResponse(HttpServletRequest request) throws IOException, ServletException
	{
		String subjectName = (String) request.getAttribute("subjectName");
		if (subjectName == null || subjectName.isEmpty())
		{
			System.out.println("Did not login while on Login page! (probably clicked one of the up-right buttons)");
			return;
		}
		createLoginSubject(subjectName, request);
	}

	private void createLoginSubject(String subjectName, HttpServletRequest request)
	{
		ValidateUserSession validation = new ValidateUserSession(request);
		if (validation.isFreshIP())
		{
			Subject newSubject = createNewSubject(request);
			newSubject = validation.updateSubject(newSubject);
			subjectDAO.updateSubjectOriginal(newSubject.getId(), true);
		}
		else
		{
			Subject newSubject = createNewSubject(request);
			newSubject = validation.updateSubject(newSubject);
			
			// User is tainted (risk != 0)
			subjectDAO.updateSubjectOriginal(newSubject.getId(), false);
			String sessionTag = (String) request.getAttribute("session");
			increaseSessionRisk(sessionTag, 10);
		}
	}
	
	private Subject createNewSubject(HttpServletRequest request)
	{
		String sessionTag = (String) request.getAttribute("session");
		String subjectName = (String) request.getAttribute("subjectName");
		
		System.out.println("Upsert");
		System.out.println(" - for session: " + sessionTag);
		System.out.println(" - create new subject: " + subjectName);
		
		Session session = sessionDAO.findSessionByTag(sessionTag);
		Subject subject = subjectDAO.insertSubject(subjectName);
		
		sessionDAO.updateSessionSubjectId(session.getId(), subject.getId());
		
		return subject;
	}
	
//	@Deprecated
//	private Subject loginExistingSubject(HttpServletRequest request)
//	{
//		String sessionTag = (String) request.getAttribute("session");
//		String subjectName = (String) request.getAttribute("subjectName");
//		
//		boolean forceSelectedUser = Boolean.parseBoolean((String) request.getAttribute("forceSelectedUser"));
//		boolean existingUser = Boolean.parseBoolean((String) request.getAttribute("existingUser"));
//		
//		
//		System.out.println("Upsert");
//		System.out.println(" - for session: " + sessionTag);
//		System.out.println(" - add existing subject: " + subjectName);
//
//		subjectDAO.insertSubject(subjectName);
//		Session session = sessionDAO.findSessionByTag(sessionTag);
//		Subject subject = subjectDAO.findSubjectsByNameAndComplete(subjectName, true).get(0);
//		
//		sessionDAO.updateSessionSubjectId(session.getId(), subject.getId());
//		
//		return subject;
//	}
	
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
	
	
	private void createTermsAndAgreementsPopup(WebpageBuilder builder)
	{
		String text = "<p>This is my start text</p>";
		new Popup(builder, "termsPopup").setOpenButton("termspopuplink").setText(text).addBottomCloseButton("m_closeButton", "Done").set();

	}

}
