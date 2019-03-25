package grenc.masters.servlets.helper;

import javax.servlet.ServletContext;

import grenc.masters.resources.PageElement;
import grenc.masters.webpage.builder.PopupBuilderAbstract;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.masters.webpage.common.Popup;


public class MatchstickTaskInfoPopup extends PopupBuilderAbstract
{	
	
	public MatchstickTaskInfoPopup(WebpageBuilder builder, ServletContext servletContext)
	{
		super(builder, servletContext);
	}

	public void createPopup(String lang)
	{
		PageElement popupContentFile;
		switch(lang)
		{
			case "en":
			default:
				popupContentFile = PageElement.info_en;
				break;
				
			case "si":
				popupContentFile = PageElement.info_si;
				break;
				
			case "sk":
				popupContentFile = PageElement.info_sk;
				break;
				
			case "at":
				popupContentFile = PageElement.info_at;
				break;
		}
		String content = readFile(popupContentFile.path());
		new Popup(builder, "infoPopup").setOpenButton("button-info").setWidth("45%").setText(content).addBottomCloseButton("m_closeButton", "Done").set();
	}
	
}
