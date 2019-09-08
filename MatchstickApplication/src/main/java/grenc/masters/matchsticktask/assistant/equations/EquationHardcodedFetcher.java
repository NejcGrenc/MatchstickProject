  package grenc.masters.matchsticktask.assistant.equations;

import java.util.HashMap;

import grenc.masters.database.equationgroups.EquationSolutionsGroupType;
import grenc.simpleton.annotation.Bean;


@Bean
public class EquationHardcodedFetcher
{
	private HashMap<EquationSolutionsGroupType, String[]> equationMap;
	
	public EquationHardcodedFetcher() {
		equationMap = new HashMap<>();
		equationMap.put(EquationSolutionsGroupType.group_1N_2N, array("2*3-3=5", "5/3+2=3"));
		equationMap.put(EquationSolutionsGroupType.group_1N, 	array("5+5-5=3", "2/2*3=2"));
		equationMap.put(EquationSolutionsGroupType.group_1MO, 	array("5-5+2=3", "3*2/3=3"));
		equationMap.put(EquationSolutionsGroupType.group_1O, 	array("3+3-5=5", "3*3-2=3"));
		equationMap.put(EquationSolutionsGroupType.group_1O_2O, array("5/5+5=5", "2/5+5=2"));
	}
	
	private String[] array(String... equations) {
		return equations;
	}
	
	
	public String equationForGroupAndTaskNo(EquationSolutionsGroupType type, int taskNo) {
		if (! equationMap.containsKey(type))
			throw new RuntimeException("Unknown EquationSolutionsGroupType: " + type.name());
		if (taskNo < 1 || taskNo > 2) 
			throw new RuntimeException("Only two equations per group type. Requested equation number: " + taskNo);

		return equationMap.get(type)[taskNo-1];
	}
}
