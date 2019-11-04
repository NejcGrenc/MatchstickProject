package grenc.masters.servlets.delegate.popup;

import javax.servlet.ServletContext;

import com.google.gson.Gson;

import grenc.growscript.base.conditional.ConditionalParameters;
import grenc.growscript.base.interfaces.ConditionalGrowSegment;
import grenc.growscript.service.GrowScriptProcessor;
import grenc.masters.matchsticktask.type.MatchstickGroup;
import grenc.masters.matchsticktask.type.MatchstickGroupLearnedStrategies;
import grenc.masters.resources.PageElement;
import grenc.masters.webpage.builder.ReadFileBuilderAbstract;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.masters.webpage.common.Popup;
import grenc.masters.webpage.translations.SimpleTranslatableSegment;
import grenc.masters.webpage.translations.Translation;
import grenc.simpleton.annotation.Bean;

@Bean
public class MatchstickTaskInfoPopup
{	 
	protected static enum ArgumentType {
		number,
		operator;
	}
	
	public void createPopup(WebpageBuilder builder, ServletContext servletContext, String lang, MatchstickGroup group, Boolean isLearningStage)
	{
		Page matchstickTaskInfoPopupPage = new Page(servletContext);
		
		AdditionalArgumentData argParam = new AdditionalArgumentData();
		argParam.language = lang;
		if (! isLearningStage)
		{
			argParam.shouldAddAdditionalArgumentNumber = false;
			argParam.shouldAddAdditionalArgumentOperator = false;
		}
		else
		{
			MatchstickGroupLearnedStrategies learnedStrategy = group.getLearnedStrategy();
			argParam.shouldAddAdditionalArgumentNumber =   (learnedStrategy == MatchstickGroupLearnedStrategies.STRATEGY_A || learnedStrategy == MatchstickGroupLearnedStrategies.BOTH);
			argParam.shouldAddAdditionalArgumentOperator = (learnedStrategy == MatchstickGroupLearnedStrategies.STRATEGY_B || learnedStrategy == MatchstickGroupLearnedStrategies.BOTH);
		}
		
		ConditionalParameters params = new ConditionalParameters();
		params.addParameter(Page.class, lang);
		params.addParameter(SimpleTranslatableSegment.class, lang);
		params.addParameter(AdditionalArgumentPretext.class, argParam);
		params.addParameter(AdditionalArgument.class, argParam);
		
		String content = new GrowScriptProcessor().process(matchstickTaskInfoPopupPage, params);
		new Popup(builder, "infoPopup").setOpenButton("button-info").setWidth("45%").setText(content).addBottomCloseButton("m_closeButton", "Done").set();
	}
	
	@SuppressWarnings("unused")
	private class Page extends ReadFileBuilderAbstract implements ConditionalGrowSegment<String> 
	{
		private ServletContext servletContext;
		
		private AdditionalArgument additional_argument_number;
		private AdditionalArgument additional_argument_operator;
		private SimpleTranslatableSegment additional_argument_text_number;
		private SimpleTranslatableSegment additional_argument_text_operator;
		private SimpleTranslatableSegment for_example;
		private AdditionalArgumentPretext for_example_or_also;
	
		public Page(ServletContext servletContext)
		{
			this.servletContext = servletContext;
			
			additional_argument_number = new AdditionalArgument(ArgumentType.number);
			additional_argument_operator = new AdditionalArgument(ArgumentType.operator);
			additional_argument_text_number = new SimpleTranslatableSegment(servletContext, "translations/matchstick-info-popup/number_text.json");
			additional_argument_text_operator = new SimpleTranslatableSegment(servletContext, "translations/matchstick-info-popup/operator_text.json");
			for_example = new SimpleTranslatableSegment(servletContext, "translations/matchstick-info-popup/for_example.json");
			for_example_or_also = new AdditionalArgumentPretext(servletContext, 
					"translations/matchstick-info-popup/also.json", "translations/matchstick-info-popup/for_example.json");
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
		private ArgumentType argumentType;
		
		AdditionalArgument(ArgumentType argumentType)
		{
			this.argumentType = argumentType;
		}
		
		@Override
		public String getBaseText() { return "Additional missing!"; }
	
		@Override
		public String getConditionalText(AdditionalArgumentData param)
		{
			Boolean addAdditionalArgument = (argumentType == ArgumentType.number) ? param.shouldAddAdditionalArgumentNumber : param.shouldAddAdditionalArgumentOperator;
			return (addAdditionalArgument) ? "block" : "none";
		}		
	}

	private class AdditionalArgumentPretext extends ReadFileBuilderAbstract implements ConditionalGrowSegment<AdditionalArgumentData>
	{
		private ServletContext context;
		private String fileAlso;
		private String fileForExample;
		
		public AdditionalArgumentPretext(ServletContext context, String fileAlso, String fileForExample) 
		{
			this.context = context;
			this.fileAlso = fileAlso;
			this.fileForExample = fileForExample;
		}
		
		@Override
		public String getBaseText() { return "Additional missing!"; }
		
		@Override
		public String getConditionalText(AdditionalArgumentData param)
		{
			String selectedFile;
			if (param.shouldAddAdditionalArgumentNumber && param.shouldAddAdditionalArgumentOperator) 
				selectedFile = fileAlso;
			else
				selectedFile = fileForExample;

			String fullJsonText = readFile(context, selectedFile);
			Translation translation = new Gson().fromJson(fullJsonText, Translation.class);
			return selectTranslation(translation, param.language);
		}	
		
		private String selectTranslation(Translation translation, String language) {
			switch(language) {
				case "en":
					return translation.getEn();
				case "si":
					return translation.getSi();
				case "de":
					return translation.getDe();
				case "sk":
					return translation.getSk();
				default:
					return getBaseText();
			}
		}
	}

	private class AdditionalArgumentData
	{
		Boolean shouldAddAdditionalArgumentNumber;
		Boolean shouldAddAdditionalArgumentOperator;
		String language;
	}
}

