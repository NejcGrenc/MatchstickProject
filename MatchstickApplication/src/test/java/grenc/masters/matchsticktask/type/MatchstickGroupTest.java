package grenc.masters.matchsticktask.type;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MatchstickGroupTest
{

	@Test
	public void shouldGetPhaseForTaskNumber_groupA() {
		assertEquals(MatchstickExperimentPhase.LearningPhase_Showing, MatchstickGroup.group_A.getPhaseForTaskNumber(1));
		assertEquals(MatchstickExperimentPhase.LearningPhase_Showing, MatchstickGroup.group_A.getPhaseForTaskNumber(2));
		assertEquals(MatchstickExperimentPhase.LearningPhase_Solving, MatchstickGroup.group_A.getPhaseForTaskNumber(3));
		assertEquals(MatchstickExperimentPhase.LearningPhase_Solving, MatchstickGroup.group_A.getPhaseForTaskNumber(4));
		assertEquals(MatchstickExperimentPhase.TestingPhase_OnlyOriginalStrategy, MatchstickGroup.group_A.getPhaseForTaskNumber(5));
		assertEquals(MatchstickExperimentPhase.TestingPhase_OnlyOriginalStrategy, MatchstickGroup.group_A.getPhaseForTaskNumber(6));
		assertEquals(MatchstickExperimentPhase.TestingPhase_OriginalStrategyOptimal, MatchstickGroup.group_A.getPhaseForTaskNumber(7));
		assertEquals(MatchstickExperimentPhase.TestingPhase_OriginalStrategyOptimal, MatchstickGroup.group_A.getPhaseForTaskNumber(8));
		assertEquals(MatchstickExperimentPhase.TestingPhase_BothStrategiesOptimal, MatchstickGroup.group_A.getPhaseForTaskNumber(9));
		assertEquals(MatchstickExperimentPhase.TestingPhase_BothStrategiesOptimal, MatchstickGroup.group_A.getPhaseForTaskNumber(10));
		assertEquals(MatchstickExperimentPhase.TestingPhase_OppositeStrategyOptimal, MatchstickGroup.group_A.getPhaseForTaskNumber(11));
		assertEquals(MatchstickExperimentPhase.TestingPhase_OppositeStrategyOptimal, MatchstickGroup.group_A.getPhaseForTaskNumber(12));
		assertEquals(MatchstickExperimentPhase.TestingPhase_OnlyOppositeStrategy, MatchstickGroup.group_A.getPhaseForTaskNumber(13));
		assertEquals(MatchstickExperimentPhase.TestingPhase_OnlyOppositeStrategy, MatchstickGroup.group_A.getPhaseForTaskNumber(14));
		assertEquals(null, MatchstickGroup.group_A.getPhaseForTaskNumber(15));
	}
	
	@Test
	public void shouldGetPhaseForTaskNumber_group0() {
		assertEquals(MatchstickExperimentPhase.TestingPhase_OnlyOriginalStrategy, MatchstickGroup.group_0_strategyA.getPhaseForTaskNumber(1));
		assertEquals(MatchstickExperimentPhase.TestingPhase_OnlyOriginalStrategy, MatchstickGroup.group_0_strategyA.getPhaseForTaskNumber(2));
		assertEquals(MatchstickExperimentPhase.TestingPhase_OriginalStrategyOptimal, MatchstickGroup.group_0_strategyA.getPhaseForTaskNumber(3));
		assertEquals(MatchstickExperimentPhase.TestingPhase_OriginalStrategyOptimal, MatchstickGroup.group_0_strategyA.getPhaseForTaskNumber(4));
		assertEquals(MatchstickExperimentPhase.TestingPhase_BothStrategiesOptimal, MatchstickGroup.group_0_strategyA.getPhaseForTaskNumber(5));
		assertEquals(MatchstickExperimentPhase.TestingPhase_BothStrategiesOptimal, MatchstickGroup.group_0_strategyA.getPhaseForTaskNumber(6));
		assertEquals(MatchstickExperimentPhase.TestingPhase_OppositeStrategyOptimal, MatchstickGroup.group_0_strategyA.getPhaseForTaskNumber(7));
		assertEquals(MatchstickExperimentPhase.TestingPhase_OppositeStrategyOptimal, MatchstickGroup.group_0_strategyA.getPhaseForTaskNumber(8));
		assertEquals(MatchstickExperimentPhase.TestingPhase_OnlyOppositeStrategy, MatchstickGroup.group_0_strategyA.getPhaseForTaskNumber(9));
		assertEquals(MatchstickExperimentPhase.TestingPhase_OnlyOppositeStrategy, MatchstickGroup.group_0_strategyA.getPhaseForTaskNumber(10));
		assertEquals(null, MatchstickGroup.group_0_strategyA.getPhaseForTaskNumber(11));
	}
}
