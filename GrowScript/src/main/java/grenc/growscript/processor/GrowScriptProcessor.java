package grenc.growscript.processor;

import java.util.List;

import grenc.growscript.base.interfaces.GrowSegment;
import grenc.growscript.parser.MoustacheParser;
import grenc.growscript.processor.handler.GrowScriptSegmentVariableHandler;
import grenc.growscript.processor.handler.GrowScriptSubSegmentHandler;


public class GrowScriptProcessor
{
	private GrowScriptSegmentVariableHandler variableHandler = new GrowScriptSegmentVariableHandler();
	private GrowScriptSubSegmentHandler subSegmentHandler = new GrowScriptSubSegmentHandler();
	
	public String process(GrowSegment segment)
	{		
		String processedText = segment.getBaseText();
				
		List<String> textVariables = variableHandler.variablesOf(segment);
		for (String variable : textVariables)
		{		
			GrowSegment subSegment = subSegmentHandler.getSubSegment(segment, variable);
			String processedVar = process(subSegment);
			
			processedText = new MoustacheParser().replaceVariable(processedText, variable, processedVar);
		}
		
		return processedText;
	}
}
