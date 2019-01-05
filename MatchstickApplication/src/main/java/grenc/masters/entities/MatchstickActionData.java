package grenc.masters.entities;

import grenc.masters.matchsticktask.type.MatchstickActionType;

public class MatchstickActionData
{
	private int id;
	private int matchstickTaskId;
	private MatchstickActionType type;
	private String startEq;
	private String endEq;
	private String startMatchstickLoc;
	private String endMatchstickLoc;
	private long startTime;
	private long endTime;
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public int getMatchstickTaskId()
	{
		return matchstickTaskId;
	}
	public void setMatchstickTaskId(int matchstickTaskId)
	{
		this.matchstickTaskId = matchstickTaskId;
	}

	public MatchstickActionType getType()
	{
		return type;
	}
	public void setType(MatchstickActionType type)
	{
		if (type == null)
			return;
		this.type = type;
	}
	public void setType(String type)
	{	
		if (type == null)
			return;
		this.type = MatchstickActionType.valueOf(type);
	}
	
	public String getStartEq()
	{
		return startEq;
	}
	public void setStartEq(String startEq)
	{
		this.startEq = startEq;
	}
	public String getEndEq()
	{
		return endEq;
	}
	public void setEndEq(String endEq)
	{
		this.endEq = endEq;
	}
	public String getStartMatchstickLoc()
	{
		return startMatchstickLoc;
	}
	public void setStartMatchstickLoc(String startMatchstickLoc)
	{
		this.startMatchstickLoc = startMatchstickLoc;
	}
	public String getEndMatchstickLoc()
	{
		return endMatchstickLoc;
	}
	public void setEndMatchstickLoc(String endMatchstickLoc)
	{
		this.endMatchstickLoc = endMatchstickLoc;
	}
	public long getStartTime()
	{
		return startTime;
	}
	public void setStartTime(long startTime)
	{
		this.startTime = startTime;
	}
	public long getEndTime()
	{
		return endTime;
	}
	public void setEndTime(long endTime)
	{
		this.endTime = endTime;
	}
	
	@Override
	public String toString() {
		return "MatchstickActionData [id=" + id + ", matchstickTaskId=" + matchstickTaskId + ", type=" + type
				+ ", startEq=" + startEq + ", endEq=" + endEq + ", startMatchstickLoc=" + startMatchstickLoc
				+ ", endMatchstickLoc=" + endMatchstickLoc + ", startTime=" + startTime + ", endTime=" + endTime + "]";
	}
	
}
