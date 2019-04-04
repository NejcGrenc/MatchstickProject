package grenc.masters.resources;

public enum PageElement {

	account_ball ("common/", "account-ball.html"),
	account_ball_popup ("common/", "account-ball-popup.html"),
	data_present_ball ("common/", "data-present-ball.html"),
	language_ball ("common/", "language-ball.html"),

	credits_ball ("speciffic/", "credits-ball.html"),
	credits ("speciffic/", "credits-main.html"),
	image_task ("speciffic/", "image-task.html"),
	login ("speciffic/", "login-main.html"),
	matchstick_task_learn ("speciffic/", "matchstick-task-learn-main.html"),
	matchstick_task_observe ("speciffic/", "matchstick-task-observe-main.html"),
	matchstick_task ("speciffic/", "matchstick-task-main.html"),
	select_language ("speciffic/", "select-language-main.html"),
	select_task ("speciffic/", "select-task-main.html"),
	task_wrapup ("speciffic/", "task-wrapup.html"),
	user_data ("speciffic/", "user-data-main.html"),
	
	agreement_en ("speciffic/agreement/", "agreement-en.html"),
	agreement_si ("speciffic/agreement/", "agreement-si.html"),
	agreement_sk ("speciffic/agreement/", "agreement-sk.html"),
	agreement_at ("speciffic/agreement/", "agreement-at.html"),
	
	matchstick_info_en ("speciffic/matchstick-info/", "info-en.html"),
	matchstick_info_si ("speciffic/matchstick-info/", "info-si.html"),
	matchstick_info_sk ("speciffic/matchstick-info/", "info-sk.html"),
	matchstick_info_at ("speciffic/matchstick-info/", "info-at.html"),
	
	image_info_en ("speciffic/image-task-info/", "info-en.html"),
	image_info_si ("speciffic/image-task-info/", "info-si.html"),
	image_info_sk ("speciffic/image-task-info/", "info-sk.html"),
	image_info_at ("speciffic/image-task-info/", "info-at.html")
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
