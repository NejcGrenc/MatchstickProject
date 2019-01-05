package grenc.masters.servlets.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ServletTest {

	@Test
	public void fetchValidServletDefinition()
	{
		String validUrl = "/login";
		Servlet servlet = Servlet.getServletDescriptionForUrl(validUrl);
		assertEquals(Servlet.LoginServlet, servlet);
	}
	
	@Test
	public void fetchServletDefinitionObBrokenUrl()
	{
		String brokenValidUrl = "login";  // Needs fixing, but is generally valid
		Servlet servlet = Servlet.getServletDescriptionForUrl(brokenValidUrl);
		assertEquals(Servlet.LoginServlet, servlet);
	}
	
	@Test
	public void fetchValidServletInstance()
	{
		String validUrl = "/login";
		BaseServlet servletInstance = Servlet.getServletInstanceForUrl(validUrl);
		assertEquals(Servlet.LoginServlet.getServletInstance(), servletInstance);
	}
	
	@Test
	public void fetchInvalidServletWithoutException()
	{
		String invalidUrl = "/completely-invalid";
		Servlet invalidServletDefinition = Servlet.getServletDescriptionForUrl(invalidUrl);
		BaseServlet invalidServletInstance = Servlet.getServletInstanceForUrl(invalidUrl);
		assertNull(invalidServletDefinition);
		assertNull(invalidServletInstance);
	}
}
