package grenc.growscript.service.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import grenc.growscript.service.utils.parser.MoustacheException;
import grenc.growscript.service.utils.parser.MoustacheParser;


public class MoustacheParserTest
{
	private MoustacheParser subject = new MoustacheParser();

	@Test
	public void shouldFindAllVariables()
	{
		List<String> expectedVariables = Arrays.asList("var1", "var2", "var3");
		List<String> returnedVariables = subject.allVariables("Some text {{var1}} is continued{{  var2}} and finished {{ var3	}}");
		assertEquals(expectedVariables, returnedVariables);
	}
	
	@Test
	public void shouldReplaceVariable()
	{
		String originalText = "Some text {{var1}} is continued{{  var2}} and finished {{ var3	}}";
		String newText = subject.replaceVariable(originalText, "var2", "HiHa");
		assertEquals("Some text {{var1}} is continuedHiHa and finished {{ var3	}}", newText);
	}
	
	@Test (expected = MoustacheException.class)
	public void shouldThrowExceptionWhenReplacingVariable()
	{
		String originalTextWithoutVariable = "Some text {{var1}} is continued and finished {{ var3	}}";
		subject.replaceVariable(originalTextWithoutVariable, "var2", "HiHa");
		fail();
	}
}
