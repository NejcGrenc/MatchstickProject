package grenc.masters.webpage.common;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;


public class DropdownSelectionTest
{
	private Map<String, String> testMap;
	
	@Before
	public void setup()
	{
		testMap = new HashMap<>();
		testMap.put("AAA", "Apple");
		testMap.put("BBB", "Orange");
	}
	
	@Test
	public void shouldCreateSelectionWithoutDefault()
	{
		DropdownSelection<String, String> selector = new DropdownSelection<>(testMap);
		
		StringBuilder expectedCode = new StringBuilder();
		expectedCode.append("<select>");
		expectedCode.append("<option value=\"AAA\">Apple</option>");
		expectedCode.append("<option value=\"BBB\">Orange</option>");
		expectedCode.append("</select>");

		assertEquals(expectedCode.toString(), selector.createHtmlCode());
	}
	
	@Test
	public void shouldCreateSelectionWithDefault()
	{
		String defaultVal = "BBB";
		DropdownSelection<String, String> selector = new DropdownSelection<>(testMap, defaultVal);
		
		StringBuilder expectedCode = new StringBuilder();
		expectedCode.append("<select>");
		expectedCode.append("<option value=\"AAA\">Apple</option>");
		expectedCode.append("<option value=\"BBB\" selected=\"selected\">Orange</option>");
		expectedCode.append("</select>");

		assertEquals(expectedCode.toString(), selector.createHtmlCode());
	}
}
