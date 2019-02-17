package grenc.masters.geolocation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import grenc.masters.uservalidation.Geolocation;

public class GeolocationTest
{
	@Test
	public void getCityDataForMatchstickTaskIP() 
	{
		Geolocation matchstickTaskServer = new Geolocation("46.36.38.196");
		
		assertEquals("Europe", matchstickTaskServer.getContinentName());
		assertEquals("Czechia", matchstickTaskServer.getCountryName());
		assertEquals("Velichovky", matchstickTaskServer.getCityName());
	}
}
