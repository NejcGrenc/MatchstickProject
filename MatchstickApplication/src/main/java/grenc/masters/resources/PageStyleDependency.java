package grenc.masters.resources;

public enum PageStyleDependency
{
	account_ball (),
	data_present_ball (),
	language_ball (),

	credits_ball (),
	credits (),
	image_task (),
	login (new Style[] {Style.centered}, new Script[] {Script.input_security}),
	matchstick_task (),
	select_language (),
	select_task (),
	task_wrapup (Script.input_security),
	user_data (Style.centered)
	;
	
	
	private Style[] styles = {};
	private Script[] scripts = {};
	
	private PageStyleDependency(Style[] styles, Script[] scripts)
	{
		this.styles = styles;
		this.scripts = scripts;
	}	
	private PageStyleDependency(Style... styles)
	{
		this.styles = styles;
	}
	private PageStyleDependency(Script... scripts)
	{
		this.scripts = scripts;
	}
	private PageStyleDependency()
	{ }
	
	public Style[] getStyles()
	{
		return styles;
	}
	public Script[] getScripts()
	{
		return scripts;
	}
}
