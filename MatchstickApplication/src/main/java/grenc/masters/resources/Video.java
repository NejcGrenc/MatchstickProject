package grenc.masters.resources;

public enum Video
{

	betweenN_mp4("video/betweenN.mp4", "video/mp4"),
	betweenN_ogg("video/betweenN.ogg", "video/ogg"),
	betweenO_mp4("video/betweenO.mp4", "video/mp4"),
	betweenO_ogg("video/betweenO.ogg", "video/ogg"),
	withinN_mp4("video/withinN.mp4", "video/mp4"),
	withinN_ogg("video/withinN.ogg", "video/ogg"),
	withinO_mp4("video/withinO.mp4", "video/mp4"),
	withinO_ogg("video/withinO.ogg", "video/ogg")
	;
	
	private String source;
	private String type;
	
	private Video(String source, String type)
	{
		this.source = source;
		this.type = type;
	}

	public String getSource()
	{
		return source;
	}
	public String getType()
	{
		return type;
	}
	
}
