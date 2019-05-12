package grenc.masters.uservalidation.countries;

public class Country
{
	private String countryCode;
	private String countryName;
	
	public Country(String countryCode, String countryName)
	{
		this.countryCode = countryCode;
		this.countryName = countryName;
	}
	
	public String getCountryCode()
	{
		return countryCode;
	}
	public String getCountryName()
	{
		return countryName;
	}

	@Override
	public String toString()
	{
		return "[" + countryCode + "] " + countryName;
	}
}
