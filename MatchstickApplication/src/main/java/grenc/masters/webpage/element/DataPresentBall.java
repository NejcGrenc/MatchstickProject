package grenc.masters.webpage.element;

import grenc.masters.database.entities.Session;
import grenc.masters.matchsticktask.type.MatchstickGroup;
import grenc.masters.resources.PageElement;
import grenc.masters.resources.Script;
import grenc.masters.resources.Style;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.simpleton.annotation.Bean;

@Bean
public class DataPresentBall
{
	private boolean allowed(Session session)
	{
		return session.isSnoopEnabled();
	}
	
	
	public DataPresentBall set(WebpageBuilder builder, Session session)
	{
		if (allowed(session))
		{
			builder.addStyle(Style.buttons);
			builder.addScript(Script.send);
			builder.appendPageElementFile(PageElement.data_present_ball);
			
			if(session.getRisk() > 0)
				builder.appendBodyScriptCommand("setRisk();");
		}
		return this;
	}
	
	public DataPresentBall withMatchstickGroup(WebpageBuilder builder, Session session, MatchstickGroup group)
	{
		if (allowed(session))
		{
			builder.appendBodyScriptCommand("setMatchstickGroup('" + group.name() + "');");
		}
		return this;
	}
}
