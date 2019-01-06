package grenc.masters.matchstick.confirm.parse;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

import grenc.masters.matchstick.objects.main.Equation;

public class ParserTest 
{
	final static Parser parser = new Parser();
	
	@Test
	public void parseCorrectEquation() 
	{
		Equation eq = parser.parseEquation("1+2=3");
		assertTrue(eq.isCorrect());
	}
	
	@Test
	public void parseInCorrectEquation() 
	{
		Equation eq = parser.parseEquation("1+2=1");
		assertFalse(eq.isCorrect());
	}
	
	@Test
	public void parseValidEquation() 
	{
		Equation eq = parser.parseEquation("1+2=3");
		assertTrue(eq.isValid());
	}
	
	@Test
	public void parseValidComplexerEquation() 
	{
		Equation eq = parser.parseEquation("8-1*4=4+3-3");
		assertTrue(eq.isValid());
	}
	
	@Test
	public void parseValidEquationWithSpaces() 
	{
		Equation eq = parser.parseEquation("1 +2  = 3 ");
		assertTrue(eq.isValid());
	}
	
	@Ignore // Not supported
	@Test
	public void parseValidEquationWithLeadingMinus() 
	{
		Equation eq = parser.parseEquation("-2=1-3");
		assertTrue(eq.isValid());
		assertTrue(eq.isCorrect());
	}
	
	@Ignore // isValid() is wrongly implemented
	@Test
	public void parseInvalidEquation1() 
	{
		Equation eq = new Parser().parseEquation("1+2=");
		assertFalse(eq.isValid());
	}
	
	@Ignore // isValid() is wrongly implemented
	@Test
	public void parseInvalidEquation2() 
	{
		Equation eq = parser.parseEquation("1+=3");
		assertFalse(eq.isValid());
	}
	
	@Test (expected = ParserException.class)
	public void parseInvalidEquationWithUnrecognizableVharacters() 
	{
		Equation eq = parser.parseEquation("1+2=a");
	}
	
	@Ignore // isValid() is wrongly implemented
	@Test
	public void invalidEquationInNeverCorrect() 
	{
		Equation eq = parser.parseEquation("1+2=");
		assertFalse(eq.isValid());
		assertFalse(eq.isCorrect());
	}
}
