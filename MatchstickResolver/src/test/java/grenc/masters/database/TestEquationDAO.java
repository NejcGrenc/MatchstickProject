package grenc.masters.database;

import java.util.List;

import grenc.masters.database.builder.QueryBuilder;


public class TestEquationDAO extends EquationDAOAbstract
{
	
	private static TestEquationDAO instance = new TestEquationDAO();
	
	private TestEquationDAO() {}
	
	public static TestEquationDAO getInstance()
	{
		return instance;
	}
	
	@Override
	protected String tableName()
	{
		return "test_equation";
	}
	
	public List<EquationWrapper> findAllEquations()
	{
		return QueryBuilder.newSelect(EquationWrapper::new)
					.fromTable(tableName())
					  .getField(EQUATION_FIELD_NAME, String.class, EquationWrapper::setEquation)
					  .orderByDesc("id", true)
					  .execute();
	}
	
}
