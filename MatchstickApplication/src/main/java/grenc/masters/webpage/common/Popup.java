package grenc.masters.webpage.common;

import grenc.masters.resources.Script;
import grenc.masters.resources.Style;
import grenc.masters.webpage.builder.WebpageBuilder;

public class Popup extends CommonElement
{
	private String popupName;
	private String popupText;
	private String buttonsCode;

	private String popupWidth = "30%";
	
	public Popup(WebpageBuilder builder, String popupName)
	{
		super(builder);
		this.popupName = popupName;
		this.buttonsCode = "";
	}

	public Popup setWidth(String width)
	{
		this.popupWidth = width;
		return this;
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
		builder.addScript(Script.translate_popup);

		createPopup();
		createCloseButton(getCloseButtonName());
		builder.appendBodyScriptCommand("translatePopup();");
	}
	
	private void createPopup() 
	{
		String popupCode = 
			"<!-- The popup -->" +
			"<div id='" + getPopupName() + "' class='modal'>" +
				"<div class='modal-margin'>"
				+ 
				  	"<!-- Popup content -->" +
				  	"<div class='popup-content modal-content closedContainer' " + style() + " >" +
				  	"	<span id='" + getCloseButtonName() + "' class='closePopup'>&times;</span>" +
				  		popupText +
				    	"<div class='closedContainer'>" + buttonsCode + "</div>" +
				    "</div>" 
				+
			    "</div>" +
		    "</div>";
		
		builder.appendPageElement(popupCode);
	}
	
	private String style()
	{
		return " style='width: " + popupWidth + "';";
	}

	private void createCloseButton(String buttonName) 
	{
		String closePopupCommand = "closePopupButton('" + buttonName + "', '" + getPopupName() + "');";
		builder.appendBodyScriptCommand(closePopupCommand);
	}
	
	
}
