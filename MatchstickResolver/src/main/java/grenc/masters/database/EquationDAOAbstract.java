package grenc.masters.database;

import grenc.masters.database.builder.QueryBuilder;


public abstract class EquationDAOAbstract 
{
	protected String EQUATION_FIELD_NAME = "equation";
	
	protected abstract String tableName();
	
	public synchronized void insert(String equation)
	{
		QueryBuilder.newInsert().intoTable(tableName())
					.setField(EQUATION_FIELD_NAME, equation)
					.execute();
	}
	
}
