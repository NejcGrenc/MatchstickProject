package grenc.masters.webpage.common;

import grenc.masters.resources.Script;
import grenc.masters.resources.Style;
import grenc.masters.webpage.builder.WebpageBuilder;

public class Popup extends CommonElement
{
	private String popupName;
	private String popupText;
	
	public Popup(WebpageBuilder builder, String popupName)
	{
		super(builder);
		this.popupName = popupName;
	}

	public void addButtonLeft(String text, String function)
	{
		
	}
	
	public void addButtonRight(String text, String function)
	{
		
	}
	
	public String getPopupName() 
	{
		return popupName;
	}
	
	public String getCloseButtonName()
	{
		return "closeButton-" + getPopupName();
	}
	
	public Popup setText(String text)
	{
		this.popupText = text;
		return this;
	}
	
	public Popup setOpenButton(String buttonId) 
	{
		builder.appendBodyScriptCommand("buttonThatOpensPopup('" + buttonId + "', '" + getPopupName() + "')");
		return this;
	}
	
	public void set()
	{
		builder.addStyle(Style.popup);
		builder.addScript(Script.popup);
		createPopup();
		createCloseButton();
	}
	
	private void createPopup() 
	{
		String popupCode = 
			"<!-- The popup -->" +
			"<div id='" + getPopupName() + "' class='modal'>" +

		  	"<!-- Popup content -->" +
		  	"<div class='modal-content'>" +
		  	"	<span id='" + getCloseButtonName() + "' class='closePopup'>&times;</span>" +
		    "	<p>" + popupText + "</p>" +
		    	createButtonCode() +
		    "</div>" +
		    "</div>";
		
		builder.appendPageElement(popupCode);
	}
	
	private String createButtonCode()
	{
		String buttonCode = 
			"<div>" +
			
			"</div>";
		return buttonCode;
	}
	
	private void createCloseButton() 
	{
		String closePopupCommand = "closePopupButton('" + getCloseButtonName() + "', '" + getPopupName() + "');";
		builder.appendBodyScriptCommand(closePopupCommand);
	}
}
