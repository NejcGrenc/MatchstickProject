package grenc.masters.webpage.translations;

import grenc.growscript.base.conditional.ConditionalParameters;
import grenc.growscript.service.GrowScriptProcessor;
import grenc.simpleton.annotation.Bean;

@Bean
public class TranslationProcessor
{
	public String process(ApplicationFileSegment pageObject, String language)
	{
		ConditionalParameters parameters = ConditionalParameters.single(SimpleTranslatableSegment.class, language);
		return new GrowScriptProcessor().process(pageObject, parameters);
	}
}
