package grenc.growscript.base;

import grenc.growscript.base.interfaces.GrowSegment;
import grenc.growscript.reader.FileReaderString;

public class FileGrowSegment implements GrowSegment
{
	private String filePath;
	
	public FileGrowSegment(String filePath)
	{
		this.filePath = filePath;
	}
	
	@Override
	public String getBaseText()
	{
		return new FileReaderString().readFile(filePath);
	}
}
