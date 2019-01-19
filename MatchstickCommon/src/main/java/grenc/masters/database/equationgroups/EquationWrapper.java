package grenc.masters.database.equationgroups;

public class EquationWrapper 
{
	private int id;
	private String equation;
	
	public void setId(int id)
	{
		this.id = id;
	}
	public int getId()
	{
		return id;
	}
	
	public void setEquation(String equation)
	{
		this.equation = equation;
	}
	public String getEquation()
	{
		return equation;
	}
}