package grenc.growscript.service;

import java.util.List;

import grenc.growscript.base.conditional.ConditionalParameters;
import grenc.growscript.base.interfaces.GrowSegment;
import grenc.growscript.service.handler.GrowScriptSegmentVariableHandler;
import grenc.growscript.service.handler.GrowScriptSubSegmentHandler;
import grenc.growscript.service.handler.SegmentTextHandler;
import grenc.growscript.service.utils.parser.MoustacheParser;


public class GrowScriptProcessor
{
	private GrowScriptSegmentVariableHandler variableHandler = new GrowScriptSegmentVariableHandler();
	private GrowScriptSubSegmentHandler subSegmentHandler = new GrowScriptSubSegmentHandler();
	private SegmentTextHandler segmentTextHandler = new SegmentTextHandler();
	
	public String process(GrowSegment segment)
	{
		return process(segment, ConditionalParameters.empty());
	}
	
	public String process(GrowSegment segment, ConditionalParameters params)
	{		
		String processedText = segmentTextHandler.getText(segment, params);
		List<String> textVariables = variableHandler.variablesOf(segment, params);
		for (String variable : textVariables)
		{		
			GrowSegment subSegment = subSegmentHandler.getSubSegment(segment, variable);
			String processedVar = process(subSegment, params);
			
			processedText = new MoustacheParser().replaceVariable(processedText, variable, processedVar);
		}
		
		return processedText;
	}
}
