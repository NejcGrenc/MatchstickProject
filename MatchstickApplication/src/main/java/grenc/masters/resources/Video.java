package grenc.masters.resources;

public enum Video
{

	first_mp4("video/first.mp4", "video/mp4"),
	first_ogg("video/first.ogg", "video/ogg")
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
