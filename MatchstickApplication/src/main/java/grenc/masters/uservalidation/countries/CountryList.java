package grenc.masters.uservalidation.countries;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import grenc.simpleton.annotation.Bean;


@Bean
public class CountryList
{
	private Map<String, Locale> countriesLocaleMap;

	public CountryList()
	{
		createCountryLocaleMap();
	}

	// TODO: keep in mind: We are mapping language and country one into other
	// For our 4 languages this is not a problem, but for others, this might be!
	public Map<String, Country> getListOfCountriesInLanguage(String lang)
	{
		lang = mapLanguage(lang);
		
		Locale languageLocale = getLocaleForCountry(lang);
		
		Map<String, Country> countryMap = new HashMap<>();	
		String[] countries = Locale.getISOCountries();
		for (String countryCode : countries)
		{
			Locale obj = new Locale("", countryCode);
			countryMap.put(obj.getCountry(), new Country(obj.getCountry(), obj.getDisplayCountry(languageLocale)));
		}
		return countryMap;
	}
	
	public String mapLanguage(String lang) {
		lang = ("en".equals(lang)) ? "gb" : lang;
		lang = ("si".equals(lang)) ? "si" : lang;
		lang = ("de".equals(lang)) ? "at" : lang;
		lang = ("sk".equals(lang)) ? "sk" : lang;
		return lang;
	}
	
	public Locale getLocaleForCountry(String countryCode)
	{
		Locale loc = countriesLocaleMap.get(countryCode.toUpperCase());
		if (loc == null)
			throw new RuntimeException("No locale found for [" + countryCode + "].");
		return loc;
	}

	private void createCountryLocaleMap()
	{
		countriesLocaleMap = new HashMap<>();
		
		Locale[] locales = Locale.getAvailableLocales();
		for (Locale obj : locales)
		{
			if (obj.getCountry() == null || obj.getCountry().isEmpty())
				continue;

			System.out.println("[Countries] --- Registering locale [" + obj.getCountry() + "]: " + obj);
			countriesLocaleMap.put(obj.getCountry(), obj);
		}
	}
	
}