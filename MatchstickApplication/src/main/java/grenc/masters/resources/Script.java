package grenc.masters.resources;

public enum Script {
	
	// TODO: Probably unused - check and remove
	user_info ("user_data/", "user_info.js"),
	user_ip ("user_data/", "user_ip.js"),


	familiar_figures ("familiar_figures_task/", "familiar_figures.js"),
	
	matchstick_calculator ("matchstick_task/", "calculator.js"),
	matchstick_equation ("matchstick_task/", "equation.js"),
	matchstick_canvas ("matchstick_task/", "matchstick_canvas.js"),
	matchstick_main ("matchstick_task/", "matchstick_main.js"),
	matchstick_matchstick ("matchstick_task/", "matchstick.js"),
	delayed_start ("matchstick_task/", "delayed_start.js"),
	
	collapsible ("collapsible.js"),
	data_present ("data_present.js"),
	page_functions ("page_functions.js"),
	popup ("popup.js"),
	send ("send.js"),
	
	translate ("translations/", "translate.js"),
	translate_credits ("translations/", "translate_credits.js"),
	translate_login ("translations/", "translate_login.js"),
	translate_selecttask ("translations/", "translate_selecttask.js"),
	translate_userdata ("translations/", "translate_userdata.js"),
	translate_familiarfigures ("translations/", "translate_familiarfigures.js"),
	translate_matchsticktask ("translations/", "translate_matchsticktask.js"),
	translate_popup ("translations/", "translate_popup.js"),
	translate_taskwrapup ("translations/", "translate_taskwrapup.js")

	;
	
	protected final String basePath = "scripts/";
	
	private String subPath;
	private String fileName;
	
	private Script(String subPath, String fileName)
	{
		this.subPath = subPath;
		this.fileName = fileName;
	}
	
	private Script(String fileName)
	{
		this.subPath = "";
		this.fileName = fileName;
	}
	
	public String path() 
	{
		return this.basePath + this.subPath + this.fileName;
	}
}
