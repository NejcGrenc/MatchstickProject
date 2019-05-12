package grenc.growscript.service.handler;

import grenc.growscript.base.conditional.ConditionalParameters;
import grenc.growscript.base.interfaces.ConditionalGrowSegment;
import grenc.growscript.base.interfaces.GrowSegment;

public class SegmentTextHandler
{
	private ConditionalGrowScriptSegmentHandler conditionalHandler = new ConditionalGrowScriptSegmentHandler();
	
	public String getText(GrowSegment segment, ConditionalParameters params)
	{
		if (conditionalHandler.isConditional(segment))
			return conditionalHandler.conditionalText((ConditionalGrowSegment<?>) segment, params);
		else
			return segment.getBaseText();
	}
}
