package grenc.masters.database.assist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import grenc.masters.database.PostgresConnector;


public class UpdateBuilder <T>
{
	PostgresConnector connector = new PostgresConnector();
	
	private String table;
	private List<FieldMapper<?>> fields = new ArrayList<>();
	private FieldMapper<Object> condition;

	
	public UpdateBuilder<T> inTable(String tableName)
	{
		this.table = tableName;
		return this;
	}
	
	public <F> UpdateBuilder<T> setField(String fieldName, F value)
	{
		FieldMapper<F> mapper = new FieldMapper<F>();
		mapper.name = fieldName;
		mapper.value = value;
		fields.add(mapper);
		return this;
	}
	
	public UpdateBuilder<T> setCondition(String fieldName, Object value)
	{
		condition = new FieldMapper<Object>();
		condition.name = fieldName;
		condition.value = value;
		return this;
	}
	
	public void execute()
	{
		try
		{
			Connection connection = connector.open();
			PreparedStatement statement = connection.prepareStatement(createStatement());
			
			int i = 0;
			for (; i < fields.size(); i++)
			{
				FieldMapper<?> field = fields.get(i);
				statement.setObject(i+1, field.value);
			}	
			statement.setObject(i+1, condition.value);
			
			statement.execute();
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally 
		{
			connector.close();
		}
	}
	
	protected String createStatement()
	{
		String fields = this.fields.stream().map(a -> a.name + " = ?").collect(Collectors.joining(", "));

		return "UPDATE " + table 
			 + " SET " + fields
			 + " WHERE " + condition.name + " = ?";
	}
	
	private class FieldMapper<O>
	{
		String name;
		O value;
	}

}
