package grenc.masters.matchsticktask.assistant;

import java.util.List;
import java.util.stream.Collectors;

import grenc.masters.database.MatchstickTaskDataDAO;
import grenc.masters.database.equationgroups.EquationSolutionsGroupType;
import grenc.masters.entities.MatchstickTaskData;
import grenc.masters.entities.TaskSession;
import grenc.masters.matchsticktask.assistant.equations.EquationDatabaseFetcher;


public class EquationAssist 
{
	private MatchstickTaskDataDAO matchstickTaskDataDAO;
	private EquationDatabaseFetcher equationDatabaseFetcher;
	
	private TaskSession taskSession; 
	
	public EquationAssist(TaskSession taskSession, MatchstickTaskDataDAO matchstickTaskDataDAO, EquationDatabaseFetcher equationDatabaseFetcher)
	{
		this.matchstickTaskDataDAO = matchstickTaskDataDAO;
		this.equationDatabaseFetcher = equationDatabaseFetcher;
		this.taskSession = taskSession;		
	}
	public EquationAssist(TaskSession taskSession)
	{
		this (taskSession, MatchstickTaskDataDAO.getInstance(), new EquationDatabaseFetcher());
	}
	
	public String getNextEquation(EquationSolutionsGroupType equationType)
	{
		List<MatchstickTaskData> previousTasks = matchstickTaskDataDAO.findAllTaskForSessionId(taskSession.getId());
		List<String> usedEquations = previousTasks.stream().map(MatchstickTaskData::getOriginalEq).collect(Collectors.toList());

		return findUnusedEquiation(equationType, usedEquations);
	}
	
	String findUnusedEquiation(EquationSolutionsGroupType equationType, List<String> usedEquations)
	{
		String newEq;
		do
		{
			newEq = equationDatabaseFetcher.fetchRandom(equationType);
		} 
		while (usedEquations.contains(newEq));
		
		return newEq;
	}
}
