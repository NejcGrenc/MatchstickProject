package grenc.masters.resources;

import java.lang.reflect.Array;

public enum PageStyleDependency
{
	account_ball (),
	data_present_ball (),
	language_ball (),

	credits_ball (),
	credits (),
	image_task (),
	login (new Style[] {Style.centered}, scripts(Script.input_security)),
	matchstick_task_learn (new Style[] {Style.style, Style.buttons}, scripts(addMatchstickAssortment(), Script.send, Script.delayed_start, Script.matchstick_learn)),
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
	
	
	private static Script[] scripts(Script[] scripts1, Script... scripts2)
	{
		return concatenate(scripts1, scripts2);
	}
	private static Script[] scripts(Script... scripts)
	{
		return scripts;
	}
	

	private static Script[] addMatchstickAssortment()
	{
		return new Script[] {Script.matchstick_main, Script.matchstick_canvas, Script.matchstick_matchstick,
				Script.matchstick_equation,Script.matchstick_calculator};	
	}
	
	private static <T> T[] concatenate(T[] a, T[] b) {
	    int aLen = a.length;
	    int bLen = b.length;

	    @SuppressWarnings("unchecked")
	    T[] c = (T[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
	    System.arraycopy(a, 0, c, 0, aLen);
	    System.arraycopy(b, 0, c, aLen, bLen);

	    return c;
	}
}
