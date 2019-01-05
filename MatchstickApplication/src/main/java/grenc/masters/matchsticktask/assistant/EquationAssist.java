package grenc.masters.matchsticktask.assistant;

import java.util.List;
import java.util.stream.Collectors;

import grenc.masters.database.MatchstickTaskDataDAO;
import grenc.masters.entities.MatchstickTaskData;
import grenc.masters.entities.TaskSession;
import grenc.masters.matchsticktask.type.MatchstickGroup;


public class EquationAssist 
{
	private MatchstickTaskDataDAO matchstickTaskDataDAO;
	
	private TaskSession taskSession; 
	
	public EquationAssist(TaskSession taskSession, MatchstickTaskDataDAO matchstickTaskDataDAO)
	{
		this.matchstickTaskDataDAO = matchstickTaskDataDAO;
		this.taskSession = taskSession;
	}
	public EquationAssist(TaskSession taskSession)
	{
		this (taskSession, MatchstickTaskDataDAO.getInstance());
	}
	
	public String getNextEquation()
	{
		List<MatchstickTaskData> previousTasks = matchstickTaskDataDAO.findAllTaskForSessionId(taskSession.getId());
		List<String> usedEquations = previousTasks.stream().map(MatchstickTaskData::getOriginalEq).collect(Collectors.toList());
		
		int kk = 0;
		String newEq = getEquationForGroup(taskSession.getMatchstickGroup(), kk);
		while (usedEquations.contains(newEq))
		{
			kk++;
			newEq = getEquationForGroup(taskSession.getMatchstickGroup(), kk);
		}
		
		return newEq;
	}

	// TODO Do!
	private String getEquationForGroup(MatchstickGroup group, int kk)
	{
		kk++;
		if (kk == 1)
			return "6-7=7";
		if(kk == 2)
			return "7+6=9";
		if(kk==2)
			return "1+6=9";
		
		return "0-1=7";
	}
	
}
