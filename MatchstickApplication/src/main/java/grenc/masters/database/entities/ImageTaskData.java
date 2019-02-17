package grenc.masters.database.entities;

public class ImageTaskData
{
	private int id;
	private int taskSessionId;
	private int number;
	private String imageId;
	private long time;
	private boolean correct;
	
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public int getTaskSessionId()
	{
		return taskSessionId;
	}
	public void setTaskSessionId(int taskSessionId)
	{
		this.taskSessionId = taskSessionId;
	}
	public int getNumber()
	{
		return number;
	}
	public void setNumber(int number)
	{
		this.number = number;
	}
	public String getImageId()
	{
		return imageId;
	}
	public void setImageId(String imageId)
	{
		this.imageId = imageId;
	}
	public long getTime()
	{
		return time;
	}
	public void setTime(long time)
	{
		this.time = time;
	}
	public boolean isCorrect()
	{
		return correct;
	}
	public void setCorrect(boolean correct)
	{
		this.correct = correct;
	}
	
	@Override
	public String toString()
	{
		return "ImageTaskData [id=" + id + ", taskSessionId=" + taskSessionId + ", number=" + number + ", imageId="
				+ imageId + ", time=" + time + ", correct=" + correct + "]";
	}
	
}
