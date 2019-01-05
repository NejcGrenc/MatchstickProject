package grenc.masters.entities;

public class MatchstickActionLocation
{
	private String posShadowInFrame;
	private String frameType;
	private int posFrameInEquation;
	
	public String getPosShadowInFrame()
	{
		return posShadowInFrame;
	}
	public void setPosShadowInFrame(String posShadowInFrame)
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