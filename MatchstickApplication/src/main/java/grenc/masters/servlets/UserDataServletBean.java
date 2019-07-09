package grenc.masters.servlets;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import grenc.growscript.base.FileGrowSegment;
import grenc.growscript.base.SimpleGrowSegment;
import grenc.growscript.service.GrowScriptProcessor;
import grenc.masters.database.dao.SessionDAO;
import grenc.masters.database.dao.SubjectDAO;
import grenc.masters.database.entities.Session;
import grenc.masters.database.entities.Subject;
import grenc.masters.resources.PageElement;
import grenc.masters.resources.Script;
import grenc.masters.resources.Style;
import grenc.masters.servlets.bean.base.BasePageServlet;
import grenc.masters.uservalidation.countries.Country;
import grenc.masters.uservalidation.countries.CountryList;
import grenc.masters.uservalidation.education.EducationLevel;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.masters.webpage.common.AccountBall;
import grenc.masters.webpage.common.DataPresentBall;
import grenc.masters.webpage.common.DropdownSelection;
import grenc.masters.webpage.common.LanguageBall;
import grenc.masters.webpage.common.Translate;
import grenc.simpleton.annotation.Bean;
import grenc.simpleton.annotation.InsertBean;


@Bean
public class UserDataServletBean extends BasePageServlet
{
	@InsertBean
	private SessionDAO sessionDAO;
	@InsertBean
	private SubjectDAO subjectDAO;
	
	@InsertBean
	private AccountBall accountBall;
	
	@InsertBean
	private CountryList countryList;

	@Override
	public String url()
	{
		return "/userData";
	}

	@Override
	protected void createWebPage(WebpageBuilder builder, HttpServletRequest request, ServletContext servletContext)
	{
		builder.setTitle("Experiments - Select language");
		
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
		new Translate(builder, Script.translate_userdata)
			.translateAll()
			.translateSpecial("m_userdata_input_age", "placeholder");
		accountBall.set(builder, servletContext);
		new DataPresentBall(builder, session).set();

		
		Map<String, Country> countryMap = countryList.getListOfCountriesInLanguage(session.getLang());
		UserDataPage userDataPage = new UserDataPage(servletContext, PageElement.user_data)
										.withCountries(countryMap, session.getLang())
										.withEducation(EducationLevel.descriptionMap(), EducationLevel.SELECT.name());
		String content = new GrowScriptProcessor().process(userDataPage);
		builder.appendPageElement(content);
	}
	
	@Override
	public void processClientsResponse(HttpServletRequest request, ServletContext servletContext) throws IOException, ServletException
	{
		provideDataToSubject(request);
	}

	private void provideDataToSubject(HttpServletRequest request)
	{
		String sessionTag = (String) request.getAttribute("session");
		
		String ageStr = (String) request.getAttribute("userdata_age");
		Integer age = (ageStr != null) ? Integer.parseInt(ageStr) : null;
		String sex = (String) request.getAttribute("userdata_sex");
		String countryType = (String) request.getAttribute("userdata_country");
		String education = (String) request.getAttribute("userdata_education");

		System.out.println("Upsert");
		System.out.println(" - for session: " + sessionTag);
		System.out.println(" - update subject data: {" + age + ", " + sex + ", " + countryType + ", " + education + "}");

		Session session = sessionDAO.findSessionByTag(sessionTag);
		Subject subject = subjectDAO.findSubjectById(session.getSubjectId());
		subjectDAO.updateSubject(subject.getId(), age, sex, countryType, education);
	}
	
	
	private class UserDataPage extends FileGrowSegment
	{
		@SuppressWarnings("unused")
		private SimpleGrowSegment country_title = new SimpleGrowSegment("Country");
		@SuppressWarnings("unused")
		private DropdownSelection<String, Country> country_selection;
		
		@SuppressWarnings("unused")
		private SimpleGrowSegment education_title = new SimpleGrowSegment("Education");
		@SuppressWarnings("unused")
		private DropdownSelection<String, String> education_selection;
		
		public UserDataPage(ServletContext servletContext, PageElement pageElement)
		{
			super(servletContext.getRealPath("/") + pageElement.path());
		}
		
		public UserDataPage withCountries(Map<String, Country> countryMap, String defaultCountry)
		{
			country_selection = new DropdownSelection<>(countryMap, countryList.mapLanguage(defaultCountry).toUpperCase());
			return this;
		}
		
		public UserDataPage withEducation(Map<String, String> educationMap, String defaultEdu)
		{
			education_selection = new DropdownSelection<>(educationMap, defaultEdu);
			return this;
		}
	}
		
}