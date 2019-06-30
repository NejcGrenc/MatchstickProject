package grenc.masters.matchsticktask.assistant;

import java.util.List;
import java.util.stream.Collectors;

import grenc.masters.database.dao.MatchstickTaskDataDAO;
import grenc.masters.database.entities.MatchstickTaskData;
import grenc.masters.database.entities.TaskSession;
import grenc.masters.database.equationgroups.EquationSolutionsGroupType;
import grenc.masters.matchsticktask.assistant.equations.EquationHardcodedFetcher;
import grenc.simpleton.annotation.Bean;
import grenc.simpleton.annotation.InsertBean;

@Bean
public class EquationAssist 
{
	@InsertBean
	private MatchstickTaskDataDAO matchstickTaskDataDAO;
	@InsertBean
	private EquationHardcodedFetcher equationFetcher;

	public String getNextEquation(EquationSolutionsGroupType equationType, int taskNumber)
	{
		return equationFetcher.equationForGroupAndTaskNo(equationType, taskNumber);
	}
	
	public String getLastUsedEquation(TaskSession taskSession)
	{
		List<String> usedEquations = findUsedEquations(taskSession.getId());
		return (usedEquations.isEmpty()) ? null : usedEquations.get(0);
	}
	
	private List<String> findUsedEquations(int taskSessionId)
	{
		List<MatchstickTaskData> previousTasks = matchstickTaskDataDAO.findAllTaskForSessionId(taskSessionId);
		List<String> usedEquations = previousTasks.stream().map(MatchstickTaskData::getOriginalEq).collect(Collectors.toList());
		return usedEquations;
	}
}
