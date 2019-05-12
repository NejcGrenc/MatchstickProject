package grenc.masters.uservalidation.countries;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Map;

import org.junit.Test;


public class CountryListTest
{
	private static String ENGLISH = "gb";
	private static String SLOVENE = "si";
	private static String SLOVAK  = "sk";
	private static String GERMAN  = "de";

	@Test
	public void shouldReturnAllFourCountries()
	{
		Map<String, Country> countries = new CountryList().getListOfCountriesInLanguage(SLOVENE);	
		//PrintUtils.printMap(countries);
		
		assertContainsCountry(countries, ENGLISH);
		assertContainsCountry(countries, SLOVENE);
		assertContainsCountry(countries, SLOVAK);
		assertContainsCountry(countries, GERMAN);
	}
	private void assertContainsCountry(Map<String, Country> countries, String country)
	{
		assertNotNull(countries.get(country.toUpperCase()));
	}
	
	
	@Test
	public void shouldReturnCountryInProperLanguage()
	{
		assertCountryNameByLang(ENGLISH, SLOVENE, "Slovenia");
		assertCountryNameByLang(SLOVENE, SLOVENE, "Slovenija");
		assertCountryNameByLang(SLOVAK,  SLOVENE, "Slovenia");
		assertCountryNameByLang(GERMAN,  SLOVENE, "Slowenien");
	}
	private void assertCountryNameByLang(String language, String countryCode, String expectedCountryName)
	{
		Map<String, Country> countries = new CountryList().getListOfCountriesInLanguage(language);
		Country country = countries.get(countryCode.toUpperCase());
		if (country == null)
			fail();
		assertEquals(country.getCountryName(), expectedCountryName);
	}
	
	
	@Test(expected = RuntimeException.class)
	public void shouldThrowExceptionForBadLanguage()
	{
		// "bc" does not exist!
		new CountryList().getListOfCountriesInLanguage("bc");
	}
	
}
