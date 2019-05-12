package grenc.masters.database.entities;

import java.io.Serializable;

public class Subject implements Serializable 
{
    private static final long serialVersionUID = 1L;
	
	private int id;
	private int sessionId;
	private Integer age;
	private String sex;
	private String countryCode;

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
	
	public int getSessionId()
	{
		return sessionId;
	}
	public void setSessionId(int sessionId)
	{
		this.sessionId = sessionId;
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
	
	public String getCountryCode()
	{
		return countryCode;
	}
	public void setCountryCode(String countryCode)
	{
		this.countryCode = countryCode;
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
		return "Subject [id=" + id + ", sessionId=" + sessionId + ", age=" + age + ", sex=" + sex + ", countryCode=" + countryCode
				+ ", ip=" + ip + ", address=" + address + ", operatingSystem=" + operatingSystem + ", browser="
				+ browser + ", original=" + original + "]";
	}

}
