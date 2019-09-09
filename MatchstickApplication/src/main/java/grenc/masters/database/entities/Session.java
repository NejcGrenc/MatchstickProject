package grenc.masters.database.entities;

public class Session
{
	private int id;
	private String tag;
	private int risk;
	private String lang;
	private int subjectId;
	
	private boolean snoopEnabled;
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	
	public String getTag()
	{
		return tag;
	}
	public void setTag(String tag)
	{
		this.tag = tag;
	}
	
	public int getRisk()
	{
		return risk;
	}
	public void setRisk(int risk)
	{
		this.risk = risk;
	}
	
	public String getLang()
	{
		return lang;
	}
	public void setLang(String lang)
	{
		this.lang = lang;
	}
	
	public int getSubjectId()
	{
		return subjectId;
	}
	public void setSubjectId(int subjectId)
	{
		this.subjectId = subjectId;
	}

	public boolean isSnoopEnabled() {
		return snoopEnabled;
	}
	public void setSnoopEnabled(boolean snoopEnabled) {
		this.snoopEnabled = snoopEnabled;
	}
	
	@Override
	public String toString() {
		return "Session [id=" + id + ", tag=" + tag + ", risk=" + risk + ", lang=" + lang + ", subjectId=" + subjectId
				+ ", snoopEnabled=" + snoopEnabled + "]";
	}
	
}
