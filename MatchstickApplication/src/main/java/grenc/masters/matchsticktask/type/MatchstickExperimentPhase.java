package grenc.masters.matchsticktask.type;

import java.util.Arrays;
import java.util.List;

public enum MatchstickExperimentPhase
{
	LearningPhase_Showing ("1"),
	LearningPhase_Solving ("2"),
	TestingPhase_MixedEquations ("3"),
	TestingPhase_OriginalEquationsOptimal ("4A"),
	TestingPhase_OriginalEquationsSuboptimal ("4B"),
	TestingPhase_OppositeEquationsOptimal ("5")
	;
	
	private MatchstickExperimentPhase (String orderInPicture) {}
	
	public static List<MatchstickExperimentPhase> orderedAll()
	{
		return Arrays.asList(
			LearningPhase_Showing,
			LearningPhase_Solving,
			TestingPhase_MixedEquations,
			TestingPhase_OriginalEquationsOptimal,
			TestingPhase_OriginalEquationsSuboptimal,
			TestingPhase_OppositeEquationsOptimal
		);
	}
}
