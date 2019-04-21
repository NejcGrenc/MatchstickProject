package grenc.masters.simplebean.processor.exception;

public class BeanProcessorException extends RuntimeException
{
	private static final long serialVersionUID = -1247192993836522082L;

	public BeanProcessorException(String message)
	{
		super(message);
	}
	public BeanProcessorException(String message, Exception e)
	{
		super(message, e);
	}
}
