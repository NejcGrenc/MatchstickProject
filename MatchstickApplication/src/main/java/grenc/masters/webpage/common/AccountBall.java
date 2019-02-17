package grenc.masters.webpage.common;

import grenc.masters.database.entities.Subject;
import grenc.masters.resources.PageElement;
import grenc.masters.resources.Style;
import grenc.masters.webpage.builder.WebpageBuilder;

public class AccountBall extends CommonElement
{
	private Subject subject;
	
	public AccountBall(WebpageBuilder builder, Subject subject)
	{
		super(builder);
		this.subject = subject;
	}

	public void set()
	{
		builder.addStyle(Style.buttons);
		builder.appendPageElementFile(PageElement.account_ball);
		attachPopup();
	}
	
	private void attachPopup() {
		String text = "Performing experiments as: <b>" + subject.getName() + "</b>";
		new Popup(builder, "popupName").setOpenButton("accountButton").setText(text).set();
	}
	
}
