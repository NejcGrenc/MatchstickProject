package grenc.growscript.base.interfaces;

public interface ConditionalGrowSegment<T> extends GrowSegment
{
	String getConditionalText(T parameter);
}
