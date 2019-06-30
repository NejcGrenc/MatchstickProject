package grenc.masters.servlets.delegate.popup;

import javax.servlet.ServletContext;

import grenc.growscript.base.conditional.ConditionalParameters;
import grenc.growscript.base.interfaces.ConditionalGrowSegment;
import grenc.growscript.service.GrowScriptProcessor;
import grenc.masters.matchsticktask.type.MatchstickGroup;
import grenc.masters.resources.PageElement;
import grenc.masters.webpage.builder.ReadFileBuilderAbstract;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.masters.webpage.common.Popup;
import grenc.simpleton.annotation.Bean;


@Bean
public class MatchstickTaskInfoPopup
{	 
	public void createPopup(WebpageBuilder builder, ServletContext servletContext, String lang, MatchstickGroup group, Boolean isLearningStage)
	{
		Page matchstickTaskInfoPopupPage = new Page(servletContext);
		
		AdditionalArgumentData argParam = new AdditionalArgumentData();
		argParam.language = lang;
		argParam.group = group;
		argParam.shouldAddAdditionalArgumant = isLearningStage;
		
		ConditionalParameters params = new ConditionalParameters();
		params.addParameter(Page.class, lang);
		params.addParameter(AdditionalArgument.class, argParam);
		
		String content = new GrowScriptProcessor().process(matchstickTaskInfoPopupPage, params);
		new Popup(builder, "infoPopup").setOpenButton("button-info").setWidth("45%").setText(content).addBottomCloseButton("m_closeButton", "Done").set();
	}

	
	private class Page extends ReadFileBuilderAbstract implements ConditionalGrowSegment<String> 
	{
		@SuppressWarnings("unused")
		private AdditionalArgument additional_argument = new AdditionalArgument();
	
		private ServletContext servletContext;
		public Page(ServletContext servletContext)
		{
			this.servletContext = servletContext;
		}
		
		@Override
		public String getBaseText() { return "Page missing!"; }
	
		@Override
		public String getConditionalText(String language)
		{
			PageElement popupContentFile = popupContentFile(language);
			return readFile(servletContext, popupContentFile.path());
		}
		
		private PageElement popupContentFile(String language)
		{
			switch(language)
			{
				case "en":
				default:
					return PageElement.matchstick_info_en;
					
				case "si":
					return PageElement.matchstick_info_si;
					
				case "sk":
					return PageElement.matchstick_info_sk;
					
				case "de":
					return PageElement.matchstick_info_at;
			}
		}
	
	}

	/**
	 * This additional argument is meant to add a little incentivised instruction to the task info.
	 * Should only be used during learning stages.
	 */
	private class AdditionalArgument implements ConditionalGrowSegment<AdditionalArgumentData>
	{
		@Override
		public String getBaseText() { return "AdditionalArgument missing!"; }
	
		@Override
		public String getConditionalText(AdditionalArgumentData param)
		{
			if (! param.shouldAddAdditionalArgumant)
				return "";

			if (isStrategyA(param.group))
				return getTextForStrategyA(param.language);
			else
				return getTextForStrategyB(param.language);
		}
	}
	
	private boolean isStrategyA(MatchstickGroup group)
	{
		return group.equals(MatchstickGroup.group_A) || group.equals(MatchstickGroup.group_0_strategyA) || group.equals(MatchstickGroup.group_AB_strategyA);
	}
	private String getTextForStrategyA(String language)
	{
		switch(language)
		{
			default:
			case "en":
				return "For instance, the matchstick tasks can be solved by <br />moving matches from one number to another number or within that number.";			
			case "si":
				return "For instance, the matchstick tasks can be solved by <br />moving matches from one number to another number or within that number.";			
			case "sk":
				return "For instance, the matchstick tasks can be solved by <br />moving matches from one number to another number or within that number.";			
			case "de":
				return "For instance, the matchstick tasks can be solved by <br />moving matches from one number to another number or within that number.";
		}
	}
	private String getTextForStrategyB(String language)
	{
		switch(language)
		{
			default:
			case "en":
				return "For instance, the matchstick tasks can be solved by <br />moving matches from one operator to another operator or within that operator.";			
			case "si":
				return "For instance, the matchstick tasks can be solved by <br />moving matches from one operator to another operator or within that operator.";			
			case "sk":
				return "For instance, the matchstick tasks can be solved by <br />moving matches from one operator to another operator or within that operator.";			
			case "de":
				return "For instance, the matchstick tasks can be solved by <br />moving matches from one operator to another operator or within that operator.";
		}
	}
	
	private class AdditionalArgumentData
	{
		Boolean shouldAddAdditionalArgumant;
		String language;
		MatchstickGroup group;
	}
}

