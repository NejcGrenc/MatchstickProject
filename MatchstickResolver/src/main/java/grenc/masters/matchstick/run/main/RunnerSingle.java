package grenc.masters.matchstick.run.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import grenc.masters.matchstick.confirm.parse.Parser;
import grenc.masters.matchstick.objects.changes.EquationChangeSingle;
import grenc.masters.matchstick.objects.main.Equation;
import grenc.masters.matchstick.run.filter.GroupSelector;
import grenc.masters.matchstick.run.filter.SolutionGroup;
import grenc.masters.matchstick.run.filter.SolutionsFinder;

public class RunnerSingle
{
	private static List<String> equation = Arrays.asList("2*3-3=5", "5/3+2=3", "3*5/5=2",
													"5+5-5=3", "2/2*3=2", "2*5-5=3",
													"5-5+2=3" , "3*2/3=3", "2*3-2=3",
													"3+3-5=5", "3*3-2=3", "2-5+2=5",
													"2/5+5=2", "5/5+5=5", "2+5/2=5");

	
	public static void main(String[] args) throws IOException
	{
//		for (String eq : equation)
//			single(eq); 
		file();
		
	}
	
	private static void single(String equation)
	{
		Equation currEq = new Parser().parseEquation(equation);
		
		SolutionsFinder finder = new SolutionsFinder();
		finder.setMaxMoves(2);
		List<EquationChangeSingle> correctFinalEquations = finder.getAllCorrectFinalEquations(currEq);
			
		SolutionGroup group = new GroupSelector(correctFinalEquations).findGroup();
		
		
		System.out.println(currEq + " (" + count1(correctFinalEquations) + ", " + count2(correctFinalEquations) + ")");
		System.out.println("Belongs to group: " + group.name());
		
		for(EquationChangeSingle solution : correctFinalEquations)
			System.out.println("" + solution.toString());
		
		System.out.println();
	}
	
	private static int count1(List<EquationChangeSingle> correctFinalEquations) {
		int i = 0;
		for (EquationChangeSingle s : correctFinalEquations)
			if (s.getAdvancedAction().isMove1())
				i++;
		return i;
	}
	
	private static int count2(List<EquationChangeSingle> correctFinalEquations) {
		int i = 0;
		for (EquationChangeSingle s : correctFinalEquations)
			if (s.getAdvancedAction().isMove2())
				i++;
		return i;
	}
	
	private static void file() throws IOException
	{
		ClassLoader classLoader = new RunnerSingle().getClass().getClassLoader();
		URL resource = classLoader.getResource("equations.txt");
		java.io.FileReader reader = new java.io.FileReader(new File(resource.getFile()));
		BufferedReader in = new BufferedReader(reader);
		    String str;
		    while ((str = in.readLine()) != null) 
		    {
		    	single(str);
		    }
		if (reader != null)
				reader.close();
	}

}
