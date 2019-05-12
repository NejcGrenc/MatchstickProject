package grenc.masters.servlets.helper;

import javax.servlet.ServletContext;

import grenc.masters.resources.PageElement;
import grenc.masters.webpage.builder.PopupBuilderAbstract;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.masters.webpage.common.Popup;
import grenc.simpleton.annotation.Bean;


@Bean
public class MatchstickTaskInfoPopup extends PopupBuilderAbstract
{	
	
	public void createPopup(WebpageBuilder builder, ServletContext servletContext, String lang)
	{
		PageElement popupContentFile;
		switch(lang)
		{
			case "en":
			default:
				popupContentFile = PageElement.matchstick_info_en;
				break;
				
			case "si":
				popupContentFile = PageElement.matchstick_info_si;
				break;
				
			case "sk":
				popupContentFile = PageElement.matchstick_info_sk;
				break;
				
			case "at":
				popupContentFile = PageElement.matchstick_info_at;
				break;
		}
		String content = readFile(servletContext, popupContentFile.path());
		new Popup(builder, "infoPopup").setOpenButton("button-info").setWidth("45%").setText(content).addBottomCloseButton("m_closeButton", "Done").set();
	}
	
}
