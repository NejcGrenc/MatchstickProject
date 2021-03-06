package grenc.growscript.service.reader;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import grenc.growscript.service.utils.reader.FileReaderString;


public class FileReaderStringTest
{

	@Test
	public void shouldReadFile()
	{
		String fileName = "simple/simple_text.txt";
		
		StringBuilder builder = new StringBuilder();
		
		FileReaderString subject = new FileReaderString(builder);
		subject.readFileFromResources(fileName);
		
		assertEquals("This is simple text.\nThis is second line!", builder.toString());
				
	}
}
