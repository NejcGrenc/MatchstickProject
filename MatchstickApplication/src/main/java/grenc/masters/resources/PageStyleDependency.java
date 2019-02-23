package grenc.masters.resources;

public enum PageStyleDependency
{
	account_ball (),
	data_present_ball (),
	language_ball (),

	credits_ball (),
	credits (),
	image_task (),
	login (Style.centered),
	matchstick_task (),
	select_language (),
	select_task (),
	task_wrapup (),
	user_data (Style.centered)
	;
	
	private Style[] styles;
	
	private PageStyleDependency(Style...styles)
	{
		this.styles = styles;
	}
	
	public Style[] getStyles()
	{
		return styles;
	}
}
