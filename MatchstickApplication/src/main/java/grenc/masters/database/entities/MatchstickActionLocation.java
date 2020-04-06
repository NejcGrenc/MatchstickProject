package grenc.masters.database.entities;

public class MatchstickActionLocation
{
	private int posShadowInFrame;
	private String frameType;
	private int posFrameInEquation;
	
	public int getPosShadowInFrame()
	{
		return posShadowInFrame;
	}
	public void setPosShadowInFrame(int posShadowInFrame)
	{
		this.posShadowInFrame = posShadowInFrame;
	}
	public String getFrameType()
	{
		return frameType;
	}
	public void setFrameType(String frameType)
	{
		this.frameType = frameType;
	}
	public int getPosFrameInEquation()
	{
		return posFrameInEquation;
	}
	public void setPosFrameInEquation(int posFrameInEquation)
	{
		this.posFrameInEquation = posFrameInEquation;
	}
	
	@Override
	public String toString()
	{
		return "MatchstickActionLocation [posShadowInFrame=" + posShadowInFrame + ", frameType=" + frameType
				+ ", posFrameInEquation=" + posFrameInEquation + "]";
	}

}