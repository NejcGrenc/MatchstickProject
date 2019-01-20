package grenc.masters.database.equationgroups;

import java.util.List;

import grenc.masters.database.builder.QueryBuilder;


public class EquationDAO 
{
	protected String ID_FIELD_NAME = "id";
	protected String EQUATION_FIELD_NAME = "equation";
	
	protected String tableName;
	
	EquationDAO (String tableName)
	{
		this.tableName = tableName;
	}
	
	public synchronized void insert(String equation)
	{
		QueryBuilder.newInsert().intoTable(tableName)
					.setField(EQUATION_FIELD_NAME, equation)
					.execute();
	}
	
	public int countAllEntities()
	{
		return QueryBuilder.newCount().fromTable(tableName).execute(); 
	}

	public String find(int id)
	{
		List<EquationWrapper> eqWrappList =  QueryBuilder.newSelect(EquationWrapper::new)
					.fromTable(tableName)
					  .getField(ID_FIELD_NAME, Integer.class, EquationWrapper::setId)
					  .getField(EQUATION_FIELD_NAME, String.class, EquationWrapper::setEquation)
					  .where(ID_FIELD_NAME, id)
					  .limit(1)
					  .execute();
			
		return (eqWrappList.isEmpty()) ? null : eqWrappList.get(0).getEquation();
	}
	
	@Deprecated // Use with extreme caution
	public List<EquationWrapper> findAllEquations()
	{
		return QueryBuilder.newSelect(EquationWrapper::new)
					.fromTable(tableName)
					  .getField(EQUATION_FIELD_NAME, String.class, EquationWrapper::setEquation)
					  .orderByDesc(ID_FIELD_NAME, true)
					  .execute();
	}
	
}
