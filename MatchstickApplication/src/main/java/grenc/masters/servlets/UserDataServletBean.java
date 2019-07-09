package grenc.masters.servlets;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import grenc.growscript.base.SimpleGrowSegment;
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
import grenc.masters.webpage.translations.ApplicationFileSegment;
import grenc.masters.webpage.translations.SimpleTranslatableSegment;
import grenc.masters.webpage.translations.TranslationProcessor;
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
	
	@InsertBean
	private TranslationProcessor translateProcessor;

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
		UserDataPage userDataPage = new UserDataPage(servletContext)
				.withCountries(countryMap, session.getLang())
				.withEducation(EducationLevel.descriptionMap(), EducationLevel.SELECT.name());
		
		builder.appendOnlyAssociatedPageElements(PageElement.task_wrapup);
		builder.appendPageElement(translateProcessor.process(userDataPage, session.getLang()));

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
	
	
	@SuppressWarnings("unused")
	private class UserDataPage extends ApplicationFileSegment
	{
		private SimpleTranslatableSegment userdata_intro = new SimpleTranslatableSegment(context, "translations/user-data/userdata_intro.json");
		private SimpleTranslatableSegment userdata_label_sex = new SimpleTranslatableSegment(context, "translations/user-data/userdata_label_sex.json");
		private SimpleTranslatableSegment userdata_input_sex_f = new SimpleTranslatableSegment(context, "translations/user-data/userdata_input_sex_f.json");
		private SimpleTranslatableSegment userdata_input_sex_m = new SimpleTranslatableSegment(context, "translations/user-data/userdata_input_sex_m.json");
		private SimpleTranslatableSegment userdata_input_sex_o = new SimpleTranslatableSegment(context, "translations/user-data/userdata_input_sex_o.json");
		private SimpleTranslatableSegment userdata_label_age = new SimpleTranslatableSegment(context, "translations/user-data/userdata_label_age.json");

		private SimpleGrowSegment country_title = new SimpleGrowSegment("Country");
		private DropdownSelection<String, Country> country_selection;
		
		private SimpleGrowSegment education_title = new SimpleGrowSegment("Education");
		private DropdownSelection<String, String> education_selection;
		
		public UserDataPage(ServletContext servletContext)
		{
			super(servletContext, PageElement.user_data);
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