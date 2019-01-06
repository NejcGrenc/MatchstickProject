package grenc.masters.matchstick.objects.changes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import grenc.masters.matchstick.confirm.parse.Parser;
import grenc.masters.matchstick.objects.main.Element;

public class ElementChangesTest 
{
	@Test
	public void all2MoveChangesForElement5()
	{
		Element element = new Parser().parseSymbol("5");
		Set<Element> changedSet = new ElementChanges(element).findAllChanged(2, 2);
		assertEquals(3, changedSet.size());
		assertTrue(changedSet.contains(new Parser().parseSymbol("2")));
		assertTrue(changedSet.contains(new Parser().parseSymbol("3")));
		assertTrue(changedSet.contains(new Parser().parseSymbol("5")));
	}
}
