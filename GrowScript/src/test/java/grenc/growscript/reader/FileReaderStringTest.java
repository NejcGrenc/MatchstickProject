package grenc.growscript.reader;

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
		subject.readFile(fileName);
		
		assertEquals("This is simple text.\nThis is second line!\n", builder.toString());
				
	}
}
