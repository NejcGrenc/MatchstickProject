package grenc.masters.servlets.helper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.ServletContext;

import grenc.masters.resources.PageElement;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.masters.webpage.common.Popup;

public class LoginAgreementPopup
{	
	private WebpageBuilder builder;
	private String basePath;
	
	public LoginAgreementPopup(WebpageBuilder builder, ServletContext servletContext)
	{
		this.builder = builder;
		this.basePath = servletContext.getRealPath("/");
	}
	
	public void createPopup(String lang)
	{
		PageElement popupContentFile;
		switch(lang)
		{
			case "en":
			default:
				popupContentFile = PageElement.agreement_en;
				break;
				
			case "si":
				popupContentFile = PageElement.agreement_si;
				break;
				
			case "sk":
				popupContentFile = PageElement.agreement_sk;
				break;
				
			case "at":
				popupContentFile = PageElement.agreement_at;
				break;
		}
		String content = readFile(popupContentFile.path());
		new Popup(builder, "agreementPopup").setOpenButton("button-agreement").setWidth("45%").setText(content).addBottomCloseButton("m_closeButton", "Done").set();
	}
	
	private String readFile(String fileName)
	{
		StringBuilder content = new StringBuilder();
		try 
		{
		    BufferedReader in = new BufferedReader(new FileReader(basePath + fileName));
		    String line;
		    while ((line = in.readLine()) != null) 
		    {
		    	content.append(line);
		    }
		    in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content.toString();
	}
	
}
