package grenc.masters.database.assist;

public class TestEntity
{
	private int id;
	private String name;
	private long value;
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public long getValue()
	{
		return value;
	}
	public void setValue(long value)
	{
		this.value = value;
	}
	
	@Override
	public String toString()
	{
		return "TestEntity [id=" + id + ", name=" + name + ", value=" + value + "]";
	}
}
