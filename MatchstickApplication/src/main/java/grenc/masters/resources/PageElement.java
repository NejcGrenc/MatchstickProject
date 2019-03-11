package grenc.masters.resources;

public enum PageElement {

	account_ball ("common/", "account-ball.html"),
	data_present_ball ("common/", "data-present-ball.html"),
	language_ball ("common/", "language-ball.html"),

	credits_ball ("speciffic/", "credits-ball.html"),
	credits ("speciffic/", "credits-main.html"),
	image_task ("speciffic/", "image-task.html"),
	login ("speciffic/", "login-main.html"),
	matchstick_task_learn ("speciffic/", "matchstick-task-learn-main.html"),
	matchstick_task ("speciffic/", "matchstick-task-main.html"),
	select_language ("speciffic/", "select-language-main.html"),
	select_task ("speciffic/", "select-task-main.html"),
	task_wrapup ("speciffic/", "task-wrapup.html"),
	user_data ("speciffic/", "user-data-main.html"),
	
	agreement_en ("speciffic/agreement/", "agreement-en.html"),
	
	info_en ("speciffic/info/", "info-en.html"),
	;
	
	
	/* Always make a corresponding PageStyleDependency entity. */
	
	protected final String basePath = "pages/";
	
	private String subPath;
	private String fileName;
	
	private PageElement(String subPath, String fileName)
	{
		this.subPath = subPath;
		this.fileName = fileName;
	}
	
	private PageElement(String fileName)
	{
		this.subPath = "";
		this.fileName = fileName;
	}
	
	public String path() 
	{
		return this.basePath + this.subPath + this.fileName;
	}
	
	
	public Style[] getDependentStyles()
	{
		PageStyleDependency dependency = PageStyleDependency.valueOf(this.name());
		return dependency.getStyles();
	}
	public Script[] getDependentScripts()
	{
		PageStyleDependency dependency = PageStyleDependency.valueOf(this.name());
		return dependency.getScripts();
	}
	
}
