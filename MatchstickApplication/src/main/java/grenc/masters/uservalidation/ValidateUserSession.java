package grenc.masters.uservalidation;

import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

public class ValidateUserSession
{
	private HttpServletRequest request;
	
	public ValidateUserSession(HttpServletRequest request)
	{
		this.request = request;
	}
	
	public void validate()
	{
		System.out.println();
		System.out.println("USER DATA");
		System.out.println("Client IP address: " + getClientIpAddress(request));
		BrowserDetails.get(request);
		System.out.println(new Geolocation(getClientIpAddress(request)));
		System.out.println();

	}
	
	
	
	public static String getClientIpAddress(HttpServletRequest request) {
	    String xForwardedForHeader = request.getHeader("X-Forwarded-For");
	    if (xForwardedForHeader == null) {
	        return request.getRemoteAddr();
	    } else {
	        // As of https://en.wikipedia.org/wiki/X-Forwarded-For
	        // The general format of the field is: X-Forwarded-For: client, proxy1, proxy2 ...
	        // we only want the client
	        return new StringTokenizer(xForwardedForHeader, ",").nextToken().trim();
	    }
	}
}
