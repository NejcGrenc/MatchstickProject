package grenc.masters.webpage.common;

import javax.servlet.http.HttpServletRequest;

import grenc.masters.resources.Script;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.simpleton.annotation.Bean;

@Bean
public class ReturnableBean
{
	
	public void prepareReturnTo(WebpageBuilder builder, String returnUrl)
	{
		builder.addScript(Script.send_return);
		builder.appendHeadScriptCommand("var returnUrl = '" + returnUrl + "';");
	}
	
	public void prepareReturnTo(WebpageBuilder builder, HttpServletRequest request) 
	{
		String returnUrl = (String) request.getAttribute("returnUrl");
		returnUrl = (returnUrl != null) ? returnUrl : "/";
		prepareReturnTo(builder, returnUrl);
	}
	
}
