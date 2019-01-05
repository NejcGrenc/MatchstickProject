package grenc.masters.matchstick.objects.changes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import grenc.masters.matchstick.confirm.valid.Validator;
import grenc.masters.matchstick.objects.main.Action;
import grenc.masters.matchstick.objects.main.Element;
import grenc.masters.matchstick.objects.main.Equation;


public class EquationChanges 
{
	private Equation original;
	private int equationLength;
	private Action initialAction;
	private MatchDistribution matchDistribution;
	private List<EquationChangeSingle> pendingEquations;
	
	
	public EquationChanges (Equation original, Action initialAction)
	{
		this.original = original;
		this.equationLength = original.getElements().length;
		this.initialAction = initialAction;
		this.matchDistribution = new MatchDistribution(equationLength, 
											initialAction.getAdded(), 
											initialAction.getRemoved());
		this.pendingEquations = new LinkedList<>();
	}
	
	
	public EquationChangeSingle findNext()
	{
		if (! pendingEquations.isEmpty())
		{
			return pendingEquations.remove(0);
		}
		
		while (matchDistribution.hasNext())
		{
			TempElementChanges[] tmpChanges = matchDistribution.next();
			List<List<Element>> posibleElements = findAllChangeCombinations(tmpChanges);
			if (posibleElements == null)
				continue;
			
			List<EquationChangeSingle> newEqs = getAllEquations(posibleElements);
			
			AdvancedAction aa = makeAdvancedAction(original, initialAction, tmpChanges);
			for (EquationChangeSingle ecs : newEqs)
				ecs.withAdvancedAction(aa);
				
			pendingEquations.addAll(newEqs);
			
			return findNext();
		}
		return null;
	}
	
	private List<List<Element>> findAllChangeCombinations(TempElementChanges[] tmpChanges)
	{
		List<List<Element>> possibilities = new ArrayList<>();
		for (int i = 0; i < original.getElements().length; i++)
		{
			TempElementChanges changesSingle = tmpChanges[i];
			Element originalElement = original.getElements()[i];
			
			if (changesSingle.isUnchanged())
			{
				// Shortcut
				ArrayList<Element> originalSingleElement = new ArrayList<>();
				originalSingleElement.add(originalElement);
				possibilities.add(originalSingleElement);
			}
			else
			{
				Set<Element> possibilitiesSingle = new ElementChanges(originalElement).findAllChanged(changesSingle.added, changesSingle.removed);
				if (possibilitiesSingle.isEmpty())
				{
					// An element that has no possibilities
					// This combination is not viable
					return null;
				}
				possibilities.add(new ArrayList<Element>(possibilitiesSingle));
			}
		}
		return possibilities;
	}
	
	private List<EquationChangeSingle> getAllEquations(List<List<Element>> posibleElements)
	{
		Element[] tmpEquation = new Element[equationLength];
		return findNewPossibleEquations(0, tmpEquation, posibleElements);
	}
	private List<EquationChangeSingle> findNewPossibleEquations(int currElement, Element[] currEq, List<List<Element>> possibilities)
	{
		List<EquationChangeSingle> eqList = new ArrayList<>();
		
		if (currElement == currEq.length)
		{
			EquationChangeSingle ecs = new EquationChangeSingle(original, new Equation(currEq.clone()));
			eqList.add(ecs);
		}
		else
		{
			for (int i = 0; i < possibilities.get(currElement).size(); i++)
			{
				currEq[currElement] = possibilities.get(currElement).get(i);
				eqList.addAll(findNewPossibleEquations(currElement + 1, currEq, possibilities));
			}
		}
		return eqList;
	}
	
	private AdvancedAction makeAdvancedAction(Equation eq, Action a, TempElementChanges[] tmpChanges)
	{
		AdvancedAction aa = new AdvancedAction(a);
		for (int i = 0; i < eq.getElements().length; i++)
		{
			Element curr = eq.getElements()[i];
			TempElementChanges currChanges = tmpChanges[i];
			while (currChanges.added > 0 && currChanges.removed > 0)
			{
				if (Validator.isValidNumeral(curr))
					aa.matchMovedWithinNumeral();
				else if (Validator.isValidOperator(curr))
					aa.matchMovedWithinOperator();
				else if (Validator.isValidComparator(curr))
					aa.matchMovedWithinComparator();
				currChanges.added--;
				currChanges.removed--;
			}
			while (currChanges.added > 0)
			{
				if (Validator.isValidNumeral(curr))
					aa.matchAddedToNumeral();
				else if (Validator.isValidOperator(curr))
					aa.matchAddedToOperator();
				else if (Validator.isValidComparator(curr))
					aa.matchAddedToComparator();
				currChanges.added--;
			}
			while (currChanges.removed > 0)
			{
				if (Validator.isValidNumeral(curr))
					aa.matchRemovedFromNumeral();
				else if (Validator.isValidOperator(curr))
					aa.matchRemovedFromOperator();
				else if (Validator.isValidComparator(curr))
					aa.matchRemovedFromComparator();
				currChanges.removed--;
			}
		}
		return aa;
	}


	private class TempElementChanges 
	{
		public int added;
		public int removed;

		TempElementChanges()
		{
			this.added = 0;
			this.removed = 0;
		}
		
		public boolean isUnchanged()
		{
			return added == 0 && removed == 0;
		}
	}
	
	private class MatchDistribution
	{
		private int equationLenght;		
		private int[] addedMatchesPositions;
		private int[] removedMatchesPositions;
		private boolean stillAvailable;
		
		MatchDistribution (int equationLenght, int addedMatches, int removedMatches)
		{
			this.equationLenght = equationLenght;
			this.addedMatchesPositions   = new int[addedMatches];
			this.removedMatchesPositions = new int[removedMatches];
			this.stillAvailable = true;
		}
		
		
		public boolean hasNext()
		{
			return stillAvailable;
		}
		
		public TempElementChanges[] next()
		{
			TempElementChanges[] current = current();
			
			stillAvailable = nextInSubset(0, addedMatchesPositions)
								|| nextInSubset(0, removedMatchesPositions);
			return current;
		}
		
		private boolean nextInSubset(int depth, int[] subset)
		{
			if (depth == subset.length)
			{
				return false;
			}
			else if (subset[depth] == equationLenght-1)
			{
				subset[depth] = 0;
				return nextInSubset(depth+1, subset);
			}
			else
			{
				subset[depth]++;
				return true;
			}
		}

		private TempElementChanges[] current()
		{
			if (! stillAvailable)
				return null;
			
			// Transform
			TempElementChanges[] tmpChanges = new TempElementChanges[equationLenght];
			for (int i = 0; i < tmpChanges.length; i++)
				tmpChanges[i] = new TempElementChanges();
			for (int pos : addedMatchesPositions)
				tmpChanges[pos].added++;
			for (int pos : removedMatchesPositions)
				tmpChanges[pos].removed++;
			return tmpChanges;
		}
	}
	
}
