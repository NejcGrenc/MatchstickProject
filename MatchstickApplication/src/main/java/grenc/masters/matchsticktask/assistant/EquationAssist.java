package grenc.masters.matchsticktask.assistant;

import java.util.List;
import java.util.stream.Collectors;

import grenc.masters.database.dao.MatchstickTaskDataDAO;
import grenc.masters.database.entities.MatchstickTaskData;
import grenc.masters.database.entities.TaskSession;
import grenc.masters.database.equationgroups.EquationSolutionsGroupType;
import grenc.masters.matchsticktask.assistant.equations.EquationDatabaseFetcher;
import grenc.simpleton.annotation.Bean;
import grenc.simpleton.annotation.InsertBean;

@Bean
public class EquationAssist 
{
	@InsertBean
	private MatchstickTaskDataDAO matchstickTaskDataDAO;
	@InsertBean
	private EquationDatabaseFetcher equationDatabaseFetcher;

	public String getNextEquation(EquationSolutionsGroupType equationType, TaskSession taskSession)
	{
		return findUnusedEquiation(equationType, findUsedEquations(taskSession.getId()));
	}
	
	public String getLastUsedEquation(TaskSession taskSession)
	{
		List<String> usedEquations = findUsedEquations(taskSession.getId());
		return (usedEquations.isEmpty()) ? null : usedEquations.get(0);
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
	
	private List<String> findUsedEquations(int taskSessionId)
	{
		List<MatchstickTaskData> previousTasks = matchstickTaskDataDAO.findAllTaskForSessionId(taskSessionId);
		List<String> usedEquations = previousTasks.stream().map(MatchstickTaskData::getOriginalEq).collect(Collectors.toList());
		return usedEquations;
	}
}
