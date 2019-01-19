package grenc.masters.matchstick.writer;

import java.io.PrintWriter;


public class WriterConsole extends Writer
{
	@Override
	public Writer makeNew()
	{
		writer = new PrintWriter(System.out, true);
		writeOriginalAswell = true;
		return this;
	}
}
