package grenc.masters.matchsticktask.assistant.speciffic;

import grenc.masters.database.equationgroups.EquationSolutionsGroupType;

public class LearnEquationAssist
{
	public String equationCommandForTypeAndNumber(EquationSolutionsGroupType groupType, int taskNumber)
	{
		switch(groupType)
		{
			case group_1N: 	return equationCommandFor1N(taskNumber);
			case group_1O: 	return equationCommandFor1O(taskNumber);
			case group_1MO: return equationCommandFor1MO(taskNumber);
			default:		return equationCommandFor1MO(taskNumber);
		}
	}
	
	private String equationCommandFor1N(int number)
	{
		switch(number)
		{
			default:
			case 1: return task1_betweenN();
			case 2: return task1_withinN();
		}
	}
	
	private String equationCommandFor1O(int number)
	{
		switch(number)
		{
			default:
			case 1: return task1_betweenO();
			case 2: return task1_withinO();
		}
	}
	
	private String equationCommandFor1MO(int number)
	{
		switch(number)
		{
			default:
			case 1: return task1_betweenN();
			case 2: return task1_betweenO();
			case 3: return task1_withinN();
			case 4: return task1_withinO();
		}
	}
	
	

	/*
	 * 
	 * setPlan...Shadow(sequentialElementNo, sequentialMatchstickInElementNo, directionRight)
	 * 
	 * sequentialElementNo -> element in order (strting on the left with zero)
	 * directionRight -> arrow comes pointing from the right
	 * sequentialElementNo ->
	 *
				//  Visual representation of 	//  Visual representation of 	//
			    //  the numeral frame			//  the operator frame			//
			    //      _____					//  							//
			    //   _ |__0__| _				//								//
			    //  | |       | |				//   	      _0  				//
			    //  |1|       |2|		 		//  	   /\| |/\ 2			//
			    //  |_| _____ |_|		 		//  	  _\_|_|_/_				//
			    //   _ |__3__| _				// 		 |_________| 3			//
			    //  | |       | |				//  	   / | | \ 				//
			    //  |4|       |5|				//   	   \/|_|\/ 1			//
			    //  |_| _____ |_|				//								//
			    //     |__6__|					//								//
			    //								//								//
     * 
	 */
	
	private String task1_betweenN()
	{
		StringBuilder commandBuilder = new StringBuilder();
		commandBuilder.append("var originalEquation = '" + "7+2+3=5" + "';");
		commandBuilder.append("setPlanStartShadow(0, 0, true);");
		commandBuilder.append("setPlanEndShadow(6, 4, false);");
		return commandBuilder.toString();
	}
	
	private String task1_withinN()
	{
		StringBuilder commandBuilder = new StringBuilder();
		commandBuilder.append("var originalEquation = '" + "1+2+2=6" + "';");
		commandBuilder.append("setPlanStartShadow(4, 4, false);");
		commandBuilder.append("setPlanEndShadow(4, 5, true);");
		return commandBuilder.toString();
	}
	
	private String task1_betweenO()
	{
		StringBuilder commandBuilder = new StringBuilder();
		commandBuilder.append("var originalEquation = '" + "1-2+3=0" + "';");
		commandBuilder.append("setPlanStartShadow(3, 0, false);");
		commandBuilder.append("setPlanEndShadow(1, 0, true);");
		return commandBuilder.toString();
	}
	
	private String task1_withinO()
	{
		StringBuilder commandBuilder = new StringBuilder();
		commandBuilder.append("var originalEquation = '" + "1+3-3=2" + "';");
		commandBuilder.append("setPlanStartShadow(3, 3, false);");
		commandBuilder.append("setPlanEndShadow(3, 2, true);");
		return commandBuilder.toString();
	}
	
	
}
