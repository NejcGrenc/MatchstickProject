package grenc.masters.matchsticktask.type;

import java.util.Arrays;
import java.util.List;

public enum MatchstickExperimentPhase
{
	LearningPhase_Showing ("L1"),
	LearningPhase_Solving ("L2"),
	TestingPhase_OnlyOriginalStrategy ("T3"),
	TestingPhase_OriginalStrategyOptimal ("T4"),
	TestingPhase_BothStrategiesOptimal ("T4"),
	TestingPhase_OppositeStrategyOptimal ("T4"),
	TestingPhase_OnlyOppositeStrategy ("T5")
	;
	
	private MatchstickExperimentPhase (String orderInPicture) {}
	
	public static List<MatchstickExperimentPhase> orderedAll()
	{
		return Arrays.asList(
			LearningPhase_Showing,
			LearningPhase_Solving,
			TestingPhase_OnlyOriginalStrategy,
			TestingPhase_OriginalStrategyOptimal,
			TestingPhase_BothStrategiesOptimal,
			TestingPhase_OppositeStrategyOptimal,
			TestingPhase_OnlyOppositeStrategy
		);
	}

}
