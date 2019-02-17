package grenc.masters.uservalidation;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

public class Geolocation
{
	private static final String geoDatabase = "GeoLite2-City_20190212/GeoLite2-City.mmdb";
	
	private String ip;
	
	private String continentName;
	private String countryName;
	private String cityName;
	private String postal;
	private String state;
	
	public Geolocation(String ip)
	{
		this.ip = ip;
		cityDataForIp(ip);
	}
	
	private void cityDataForIp(String ip)
	{
		try
		{
			ClassLoader classLoader = getClass().getClassLoader();
			File database = new File(classLoader.getResource(geoDatabase).getFile());
			
		    DatabaseReader dbReader = new DatabaseReader.Builder(database).build();
		         
		    InetAddress ipAddress = InetAddress.getByName(ip);
		    CityResponse response = dbReader.city(ipAddress);
		         
		    continentName = response.getContinent().getName();
		    countryName = response.getCountry().getName();
		    cityName = response.getCity().getName();
		    postal = response.getPostal().getCode();
		    state = response.getLeastSpecificSubdivision().getName();
		}
		catch (AddressNotFoundException e)
		{
	    	System.out.println("No data found for provided IP: " + ip);
			System.out.println(e);
		}
		catch (IOException e)
		{
			System.out.println("Unable to open Geolocation database");
			System.out.println(e);
		}
	    catch (GeoIp2Exception e)
		{
	    	System.out.println("No data found for provided IP: " + ip);
			System.out.println(e);
		}
	}

	public String getIp()
	{
		return ip;
	}

	public String getContinentName()
	{
		return continentName;
	}

	public String getCountryName()
	{
		return countryName;
	}

	public String getCityName()
	{
		return cityName;
	}

	public String getPostal()
	{
		return postal;
	}

	public String getState()
	{
		return state;
	}

	@Override
	public String toString()
	{
		return "Geolocation [ip=" + ip + ", continentName=" + continentName + ", countryName=" + countryName
				+ ", cityName=" + cityName + ", postal=" + postal + ", state=" + state + "]";
	}
	
}
