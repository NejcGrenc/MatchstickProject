package grenc.masters.servlets;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import grenc.growscript.base.conditional.ConditionalParameters;
import grenc.growscript.base.interfaces.ConditionalGrowSegment;
import grenc.growscript.base.interfaces.GrowSegment;
import grenc.masters.database.dao.SessionDAO;
import grenc.masters.database.dao.SubjectDAO;
import grenc.masters.database.entities.Session;
import grenc.masters.database.entities.Subject;
import grenc.masters.resources.PageElement;
import grenc.masters.resources.Script;
import grenc.masters.resources.Style;
import grenc.masters.servlets.bean.base.BasePageServlet;
import grenc.masters.uservalidation.ValidateUserSession;
import grenc.masters.webpage.builder.ReadFileBuilderAbstract;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.masters.webpage.common.DataPresentBall;
import grenc.masters.webpage.common.LanguageBall;
import grenc.masters.webpage.translations.ApplicationFileSegment;
import grenc.masters.webpage.translations.SimpleTranslatableSegment;
import grenc.masters.webpage.translations.TranslationProcessor;
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
	private TranslationProcessor translateProcessor;

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
		builder.addStyle(Style.centered);	

		builder.addScript(Script.page_functions);
		builder.addScript(Script.send);

		
		Session session = sessionDAO.findSessionByTag((String) request.getAttribute("session"));
		
		new LanguageBall(builder, session.getLang(), url()).set();

		new DataPresentBall(builder, session).set();
		
		
		builder.appendOnlyAssociatedPageElements(PageElement.login);
		ConditionalParameters params = ConditionalParameters.single(AgreementPage.class, new PageArguments(session.getLang(), servletContext));
		builder.appendPageElement(translateProcessor.process(new LoginPage(servletContext), session.getLang(), params));
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
	
	@SuppressWarnings("unused")
	private class LoginPage extends ApplicationFileSegment
	{
		private SimpleTranslatableSegment welcome = new SimpleTranslatableSegment(context, "translations/login/welcome.json");
		private SimpleTranslatableSegment acknowledgement = new SimpleTranslatableSegment(context, "translations/login/acknowledgement.json");
		private GrowSegment agreement = new AgreementPage();

		
		public LoginPage(ServletContext context)
		{
			super(context, PageElement.login);
		}
	}
	
	private class AgreementPage extends ReadFileBuilderAbstract implements ConditionalGrowSegment<PageArguments> {

		@Override
		public String getBaseText()
		{
			throw new RuntimeException("Default version is not available.");
		}

		@Override
		public String getConditionalText(PageArguments parameter)
		{
			PageElement popupContentFile = select(parameter.language);
			return readFile(parameter.servletContext, popupContentFile.path());
		}
		
		private PageElement select(String language) {
			switch(language)
			{
				default:
				case "en":
					return PageElement.agreement_en;
				case "si":
					return PageElement.agreement_si;
				case "sk":
					return PageElement.agreement_sk;
				case "de":
				case "at":
					return PageElement.agreement_at;
			}
		}
		
	}
	
	private class PageArguments {
		String language;
		ServletContext servletContext;
		
		PageArguments (String language, ServletContext servletContext) {
			this.language = language;
			this.servletContext = servletContext;
		}
	}
	
}