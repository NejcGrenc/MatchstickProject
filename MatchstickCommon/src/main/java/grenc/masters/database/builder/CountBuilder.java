package grenc.masters.database.builder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import grenc.masters.database.PostgresConnector;


public class CountBuilder
{
	PostgresConnector connector = new PostgresConnector();
	
	private String table;
	private List<Condition<?>> conditions = new ArrayList<>();
	
	public CountBuilder fromTable(String tableName)
	{
		this.table = tableName;
		return this;
	}
	
	public <V> CountBuilder where(String fieldName, V value)
	{
		Condition<V> condition = new Condition<>();
		condition.field = fieldName;
		condition.value = value;
		conditions.add(condition);
		return this;
	}

	public int execute()
	{
		int total = -1;
		try
		{
			Connection connection = connector.open();
			PreparedStatement statement = connection.prepareStatement(createStatement());
			
			for (int i = 0; i < conditions.size(); i++)
			{
				Condition<?> condition = conditions.get(i);
				statement.setObject(i+1, condition.value);
			}
			
			ResultSet rs = statement.executeQuery();
			rs.next();
			
			total = rs.getInt("total");
			rs.close();
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally 
		{
			connector.close();
		}
		return total;
	}
	
	protected String createStatement()
	{
		String conditions = (this.conditions.isEmpty()) ? "" : 
						"WHERE " + this.conditions.stream()
									   .map(cond -> cond.field + " = ?")
									   .collect(Collectors.joining(" AND "));
		return "SELECT COUNT(*) AS total"
			 + " FROM " + table 
			 + " " + conditions;
	}
	
	private class Condition<V>
	{
		String field;
		V value;
	}
}
