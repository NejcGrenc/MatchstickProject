package grenc.masters.servlets;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;

import java.io.IOException;
import java.util.HashSet;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import grenc.masters.database.dao.SessionDAO;
import grenc.masters.database.dao.SubjectDAO;
import grenc.masters.database.entities.Session;
import grenc.masters.database.entities.Subject;
import grenc.masters.servlets.base.BaseServlet;
import grenc.masters.servlets.base.DispatcherServlet;
import grenc.masters.servlets.base.Servlet;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Servlet.class, SessionDAO.class, SubjectDAO.class})
public class DispatcherServletTest
{
	private SessionDAO sessionDAO;
	private SubjectDAO subjectDAO;
	
	private DispatcherServlet servlet;
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	private static final String sessionParamName = "session";
	private static final String previousUrlParamName = "previousUrl";
	private static final String forwardUrlParamName = "forwardUrl";
	
	
	@Before
	public void setup()
	{
		sessionDAO = mock(SessionDAO.class);
		subjectDAO = mock(SubjectDAO.class);
		
		servlet = spy(new DispatcherServlet());
		
		// Return mocked dispatcher
		RequestDispatcher dispatcher = mock(RequestDispatcher.class);
		Mockito.doReturn(dispatcher).when(servlet).buildDispatcher(anyString());
		
		// Mock request / response pair
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		
		// Request 
		Mockito.when(request.getParameterNames()).thenReturn(new Vector<String>(new HashSet<String>()).elements());
	
		// Prepare static mocks
	    PowerMockito.mockStatic(Servlet.class);
	    PowerMockito.mockStatic(SessionDAO.class);
	    PowerMockito.mockStatic(SubjectDAO.class);

	    Mockito.when(SessionDAO.getInstance()).thenReturn(sessionDAO);
	    Mockito.when(SubjectDAO.getInstance()).thenReturn(subjectDAO);
	}
	
	private void verifyRedirectTo(String expectedForwardUrl)
	{
		Mockito.verify(servlet, times(1)).buildDispatcher(eq(expectedForwardUrl));
	}
	
	
	@Test
	@Ignore
	public void processesPreviousRequest() throws IOException, ServletException
	{
		// Given: request from a previous servlet
		String prevUrl = "prevUrl";
		Mockito.doReturn(prevUrl).when(request).getAttribute(previousUrlParamName);
		
		// And: a mocked servlet associated with it
		BaseServlet mockPrevServlet = mock(BaseServlet.class);
		Mockito.when(Servlet.getServletInstanceForUrl(prevUrl)).thenReturn(mockPrevServlet);
		
		// When: servlet executed
		servlet.doGet(request, response);
		
		// Then: previous servlet gets response executed
		Mockito.verify(mockPrevServlet, times(1)).processClientsResponse(eq(request));
	}
	
	
	@Test
	@Ignore
	public void dispatcherExecutesRedirect() throws IOException, ServletException
	{
		// Given: a mock of dispatcher
		RequestDispatcher mockDispatcher = mock(RequestDispatcher.class);
		Mockito.doReturn(mockDispatcher).when(servlet).buildDispatcher(anyString());

		// When: servlet executed
		servlet.doGet(request, response);
		
		// Then: dispatcher dispatches the request / response pair
		Mockito.verify(mockDispatcher, times(1)).forward(request, response);;
	}
	
//	@Test
//	public void forwardUrlIsIgnoredUntilBasicDataIsSet() throws IOException, ServletException
//	{
//		// And: forward url
//	    String forwardUrl = "/forwards";
//		Mockito.doReturn(forwardUrl).when(request).getAttribute(eq(forwardUrlParamName));
//		
//		// When: servlet executed
//		servlet.doGet(request, response);
//		
//		// Then
//		verifyRedirectTo(Servlet.SessionGeneratorServlet.getUrl());
//	}
	
	@Test
	public void providedSession_redirectToLanguageServlet() throws IOException, ServletException
	{
		// Given: session
		String sessionTag = "sessionTag";
		Mockito.doReturn(sessionTag).when(request).getAttribute(eq(sessionParamName));
	    Mockito.when(sessionDAO.findSessionByTag(sessionTag)).thenReturn(new Session());
	    
		// When: servlet executed
		servlet.doGet(request, response);
		
		// Then
		verifyRedirectTo(Servlet.LanguageServlet.getUrl());
	}
	
	@Test
	public void providedSessionWithLanguage_redirectToLoginServlet() throws IOException, ServletException
	{
		// Given: session
		String sessionTag = "sessionTag";
		Mockito.doReturn(sessionTag).when(request).getAttribute(eq(sessionParamName));

		// And: language
		Session session = new Session();
		session.setLang("si");
	    Mockito.when(sessionDAO.findSessionByTag(sessionTag)).thenReturn(session);
	    
		// When: servlet executed
		servlet.doGet(request, response);
		
		// Then
		verifyRedirectTo(Servlet.LoginServlet.getUrl());
	}

	@Test
	public void providedSessionWithLanguageAndSubject_redirectToUserDataServlet() throws IOException, ServletException
	{
		// Given: session
		String sessionTag = "sessionTag";
		Mockito.doReturn(sessionTag).when(request).getAttribute(eq(sessionParamName));

		// And: subject
		Subject subject = new Subject();
		subject.setId(1234);
	    Mockito.when(subjectDAO.findSubjectById(subject.getId())).thenReturn(subject);
		
		// And: session belongs to subject and has language set
		Session session = new Session();
		session.setLang("si");
		session.setSubjectId(subject.getId());
	    Mockito.when(sessionDAO.findSessionByTag(sessionTag)).thenReturn(session);

		// When: servlet executed
		servlet.doGet(request, response);
		
		// Then
		verifyRedirectTo(Servlet.UserDataServlet.getUrl());
	}
	
	@Test
	public void providedFullSessionAndFullSubject_redirectToForwardUrl() throws IOException, ServletException
	{
		String sessionTag = "sessionTag";
		Mockito.doReturn(sessionTag).when(request).getAttribute(eq(sessionParamName));

		// Given: full subject
		Subject subject = new Subject();
		subject.setId(1234);
		subject.setAge(15);
		subject.setName("Tony");
		subject.setSex("m");
		subject.setLanguage("si");
	    Mockito.when(subjectDAO.findSubjectById(subject.getId())).thenReturn(subject);
		
		// And: full session
		Session session = new Session();
		session.setLang("si");
		session.setSubjectId(subject.getId());
	    Mockito.when(sessionDAO.findSessionByTag(sessionTag)).thenReturn(session);
	    
	    // And: forward url
	    String forwardUrl = "/forwards";
		Mockito.doReturn(forwardUrl).when(request).getAttribute(eq(forwardUrlParamName));


		// When: servlet executed
		servlet.doGet(request, response);
		
		// Then
		verifyRedirectTo(forwardUrl);
	}
	
	@Test
	public void providedFullSessionAndFullSubjectButNoForwardUrl_redirectToSelectTaskServlet() throws IOException, ServletException
	{
		String sessionTag = "sessionTag";
		Mockito.doReturn(sessionTag).when(request).getAttribute(eq(sessionParamName));

		// Given: full subject
		Subject subject = new Subject();
		subject.setId(1234);
		subject.setAge(15);
		subject.setName("Tony");
		subject.setSex("m");
		subject.setLanguage("si");
	    Mockito.when(subjectDAO.findSubjectById(subject.getId())).thenReturn(subject);
		
		// And: full session
		Session session = new Session();
		session.setLang("si");
		session.setSubjectId(subject.getId());
	    Mockito.when(sessionDAO.findSessionByTag(sessionTag)).thenReturn(session);

		// When: servlet executed
		servlet.doGet(request, response);
		
		// Then
		verifyRedirectTo(Servlet.SelectTaskServlet.getUrl());
	}
}
