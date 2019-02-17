package grenc.masters.webpage.common;

import grenc.masters.database.entities.Session;
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

	public void set()
	{
		if (allowed())
		{
			builder.addStyle(Style.buttons);
			builder.addScript(Script.send);
			builder.appendPageElementFile(PageElement.data_present_ball);
			//builder.appendBodyScriptCommand("var currentPage = '" + currentPage + "';");
		}
	}
	
	private boolean allowed()
	{
		return session.isSnoopEnabled();
	}
}
