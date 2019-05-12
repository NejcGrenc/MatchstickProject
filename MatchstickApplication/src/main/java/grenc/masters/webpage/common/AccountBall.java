package grenc.masters.webpage.common;

import javax.servlet.ServletContext;

import grenc.masters.resources.PageElement;
import grenc.masters.resources.Style;
import grenc.masters.webpage.builder.PopupBuilderAbstract;
import grenc.masters.webpage.builder.WebpageBuilder;
import grenc.simpleton.annotation.Bean;


@Bean
public class AccountBall extends PopupBuilderAbstract
{
	public void set(WebpageBuilder builder, ServletContext servletContext)
	{
		builder.addStyle(Style.buttons);
		builder.addStyle(Style.popup);
		builder.appendPageElementFile(PageElement.account_ball);
		attachPopup(builder, servletContext);
	}
	
	private void attachPopup(WebpageBuilder builder, ServletContext servletContext) {
		String content = readFile(servletContext, PageElement.account_ball_popup.path());

		new Popup(builder, "popupName").setOpenButton("accountButton").setText(content).set();
	}
	
}
