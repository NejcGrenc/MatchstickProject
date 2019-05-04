package grenc.growscript.service.exception;

public class GrowScriptException extends RuntimeException
{
	private static final long serialVersionUID = -1484362882659024916L;

	public GrowScriptException(Throwable e)
	{
		super(e);
	}

	public GrowScriptException(String message)
	{
		super (message);
	}
}
