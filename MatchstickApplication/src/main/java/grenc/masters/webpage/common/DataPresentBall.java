package grenc.masters.webpage.common;

import grenc.masters.database.entities.Session;
import grenc.masters.matchsticktask.type.MatchstickGroup;
import grenc.masters.resources.PageElement;
import grenc.masters.resources.Script;
import grenc.masters.resources.Style;
import grenc.masters.webpage.builder.WebpageBuilder;

public class DataPresentBall extends CommonElement
{
	private Session session;
	
	// We do not have a return button (browser return works ok)
	public DataPresentBall(WebpageBuilder builder, Session session)
	{
		super(builder);
		this.session = session;
	}

	private boolean allowed()
	{
		return session.isSnoopEnabled();
	}
	
	
	public DataPresentBall set()
	{
		if (allowed())
		{
			builder.addStyle(Style.buttons);
			builder.addScript(Script.send);
			builder.appendPageElementFile(PageElement.data_present_ball);
			
			if(session.getRisk() > 0)
				builder.appendBodyScriptCommand("setRisk();");
		}
		return this;
	}
	
	public DataPresentBall withMatchstickGroup(MatchstickGroup group)
	{
		if (allowed())
		{
			builder.appendBodyScriptCommand("setMatchstickGroup('" + group.name() + "');");
		}
		return this;
	}
}
