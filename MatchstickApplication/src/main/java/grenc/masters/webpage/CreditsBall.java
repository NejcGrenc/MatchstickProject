package grenc.masters.webpage;

import grenc.masters.resources.PageElement;
import grenc.masters.resources.Script;
import grenc.masters.resources.Style;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.masters.webpage.common.CommonElement;


public class CreditsBall extends CommonElement
{
	public CreditsBall(WebpageBuilder builder)
	{
		super(builder);
	}

	public void set()
	{
		builder.addStyle(Style.buttons_credits);
		builder.addStyle(Style.layout);
		builder.addScript(Script.send);
		builder.appendPageElementFile(PageElement.credits_ball);
	}

}
