package grenc.masters.database.assist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import grenc.masters.database.PostgresConnector;


public class InsertBuilder <T>
{
	PostgresConnector connector = new PostgresConnector();
	
	private String table;
	private List<FieldMapper<?>> fields = new ArrayList<>();
	
	
	public InsertBuilder<T> intoTable(String tableName)
	{
		this.table = tableName;
		return this;
	}
	
	public <F> InsertBuilder<T> setField(String fieldName, F value)
	{
		FieldMapper<F> mapper = new FieldMapper<F>();
		mapper.name = fieldName;
		mapper.value = value;
		fields.add(mapper);
		return this;
	}
	
	public void execute()
	{
		try
		{
			Connection connection = connector.open();
			PreparedStatement statement = connection.prepareStatement(createStatement());
			
			for (int i = 0; i < fields.size(); i++)
			{
				FieldMapper<?> field = fields.get(i);
				statement.setObject(i+1, field.value);
			}
			
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
		String fields = this.fields.stream().map(a -> a.name).collect(Collectors.joining(", "));
		String markers = this.fields.stream().map(a -> "?").collect(Collectors.joining(", "));
		
		return "INSERT"
			 + " INTO " + table 
			 + " (" + fields + ")"
			 + " VALUES (" + markers + ")";
	}
	
	private class FieldMapper<O>
	{
		String name;
		O value;
	}

}
