package grenc.masters.webpage.common;

import grenc.masters.resources.Script;
import grenc.masters.resources.Style;
import grenc.masters.webpage.builder.WebpageBuilder;

public class Popup extends CommonElement
{
	private String popupName;
	private String popupText;
	private String buttonsCode;

	
	public Popup(WebpageBuilder builder, String popupName)
	{
		super(builder);
		this.popupName = popupName;
		this.buttonsCode = "";
		
		builder.addScript(Script.translate_popup);
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
	
	public Popup addBottomButton(String text, String function)
	{
		
		return this;
	}
	
	public Popup addBottomCloseButton(String buttonName, String text)
	{
		buttonsCode += "<button class='rightButton' id='" + buttonName + "'>" + text + "</button>";
		createCloseButton(buttonName);
		return this;
	}
	
	public String getPopupName() 
	{
		return popupName;
	}
	
	public String getCloseButtonName()
	{
		return "closeButton-" + getPopupName();
	}
	
	
	public void set()
	{
		builder.addStyle(Style.popup);
		builder.addScript(Script.popup);
		createPopup();
		createCloseButton(getCloseButtonName());
		builder.appendBodyScriptCommand("translatePopup();");
	}
	
	private void createPopup() 
	{
		String popupCode = 
			"<!-- The popup -->" +
			"<div id='" + getPopupName() + "' class='modal'>" +

			  	"<!-- Popup content -->" +
			  	"<div class='modal-content'>" +
			  	"	<span id='" + getCloseButtonName() + "' class='closePopup'>&times;</span>" +
			  		popupText +
			    	"<div class='buttonContainer'>" + buttonsCode + "</div>" +
			    "</div>" +
		    "</div>";
		
		builder.appendPageElement(popupCode);
	}

	private void createCloseButton(String buttonName) 
	{
		String closePopupCommand = "closePopupButton('" + buttonName + "', '" + getPopupName() + "');";
		builder.appendBodyScriptCommand(closePopupCommand);
	}
	
	
}
