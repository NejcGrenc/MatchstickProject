package grenc.masters.database.entities;

import java.io.Serializable;

public class Subject implements Serializable 
{
    private static final long serialVersionUID = 1L;
	
	private int id;
	private boolean completeData;
	private String name;
	private Integer age;
	private String sex;
	private String language;
	private String password;
	
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	
	public boolean isCompleteData()
	{
		return completeData;
	}
	public void setCompleteData(boolean completeData)
	{
		this.completeData = completeData;
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	
	public Integer getAge()
	{
		return age;
	}
	public void setAge(Integer age)
	{
		this.age = age;
	}
	
	public String getSex()
	{
		return sex;
	}
	public void setSex(String sex)
	{
		this.sex = sex;
	}
	
	public String getLanguage()
	{
		return language;
	}
	public void setLanguage(String language)
	{
		this.language = language;
	}
	
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	public boolean isMissingUserData()
	{
		return getAge() == null || getSex() == null;
	}
	
	@Override
	public String toString()
	{
		return "Subject [id=" + id + ", name=" + name + ", age=" + age + ", sex=" + sex + ", language=" + language
				+ ", password=" + password + "]";
	}
}
