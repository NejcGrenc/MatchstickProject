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
	private String ip;
	private String address;
	private String operatingSystem;
	private String browser;
	private boolean original;
	
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
	
	public boolean isMissingUserData()
	{
		return getAge() == null || getSex() == null;
	}
	
	public String getIp()
	{
		return ip;
	}
	public void setIp(String ip)
	{
		this.ip = ip;
	}
	public String getAddress()
	{
		return address;
	}
	public void setAddress(String address)
	{
		this.address = address;
	}
	public String getOperatingSystem()
	{
		return operatingSystem;
	}
	public void setOperatingSystem(String operatingSystem)
	{
		this.operatingSystem = operatingSystem;
	}
	public String getBrowser()
	{
		return browser;
	}
	public void setBrowser(String browser)
	{
		this.browser = browser;
	}
	public boolean isOriginal()
	{
		return original;
	}
	public void setOriginal(boolean original)
	{
		this.original = original;
	}
	
	@Override
	public String toString()
	{
		return "Subject [id=" + id + ", completeData=" + completeData + ", name=" + name + ", age=" + age + ", sex="
				+ sex + ", language=" + language + ", password=" + password + ", ip=" + ip + ", address=" + address
				+ ", operatingSystem=" + operatingSystem + ", browser=" + browser + ", original=" + original + "]";
	}

}
