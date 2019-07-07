package grenc.masters.webpage.translations;

import javax.servlet.ServletContext;

import com.google.gson.Gson;

import grenc.growscript.base.interfaces.ConditionalGrowSegment;
import grenc.masters.webpage.builder.ReadFileBuilderAbstract;


public class SimpleTranslatableSegment extends ReadFileBuilderAbstract implements ConditionalGrowSegment<String>
{
	private static boolean blocking = true;

	private ServletContext context;
	private String file;
	
	public SimpleTranslatableSegment(ServletContext context, String file) 
	{
		this.context = context;
		this.file = file;
	}
	
	@Override
	public String getBaseText()
	{
		if (blocking)
			throw new RuntimeException("Missing translation!");
		return "Missing translation!";
	}

	@Override
	public String getConditionalText(String language)
	{
		String fullJsonText = readFile(context, file);
		Translation translation = new Gson().fromJson(fullJsonText, Translation.class);
		return selectTranslation(translation, language);
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
