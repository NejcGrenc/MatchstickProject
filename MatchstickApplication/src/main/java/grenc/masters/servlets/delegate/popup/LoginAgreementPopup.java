package grenc.masters.servlets.delegate.popup;

import javax.servlet.ServletContext;

import grenc.masters.resources.PageElement;
import grenc.masters.webpage.builder.PopupBuilderAbstract;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.masters.webpage.common.Popup;
import grenc.simpleton.annotation.Bean;


@Bean
public class LoginAgreementPopup extends PopupBuilderAbstract
{	
	
	public void createPopup(WebpageBuilder builder, ServletContext servletContext, String lang)
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
		String content = readFile(servletContext, popupContentFile.path());
		new Popup(builder, "agreementPopup").setOpenButton("button-agreement").setWidth("45%").setText(content).addBottomCloseButton("m_closeButton", "Done").set();
	}
	
}
