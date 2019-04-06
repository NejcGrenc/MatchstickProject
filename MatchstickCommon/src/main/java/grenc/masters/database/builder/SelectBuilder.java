package grenc.masters.database.builder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import grenc.masters.database.PostgresConnector;


public class SelectBuilder <T>
{
	PostgresConnector connector = new PostgresConnector();
	
	private Supplier<T> classType;
	private String table;
	private List<FieldMapper<?>> fields = new ArrayList<>();
	private List<Condition<?>> conditions = new ArrayList<>();
	private String orderBy = null;
	private boolean orderDesc = true;
	private int limit;
	
	public SelectBuilder (Supplier<T> type)
	{
		this.classType = type;
	}
	
	public SelectBuilder<T> fromTable(String tableName)
	{
		this.table = tableName;
		return this;
	}
	
	public <F> SelectBuilder<T> getField(String fieldName, Class<F> fieldType, BiConsumer<T, F> fieldSetter)
	{
		FieldMapper<F> mapper = new FieldMapper<F>();
		mapper.name = fieldName;
		mapper.type = fieldType;
		mapper.setter = fieldSetter;
		fields.add(mapper);
		return this;
	}
	
	public <V> SelectBuilder<T> where(String fieldName, V value)
	{
		Condition<V> condition = new Condition<>();
		condition.field = fieldName;
		condition.value = value;
		conditions.add(condition);
		return this;
	}
	
	public SelectBuilder<T> orderByDesc(String fieldName, boolean desc)
	{
		this.orderBy = fieldName;
		this.orderDesc = desc;
		return this;
	}
	
	public SelectBuilder<T> limit(int limit)
	{
		this.limit = limit;
		return this;
	}
	
	public List<T> execute()
	{
		ArrayList<T> allData = new ArrayList<>();
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
			while (rs.next())
			{
				T entity = buildEntity(rs);
				allData.add(entity);
			}	
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
		return allData;
	}
	
	protected String createStatement()
	{
		String fields = this.fields.stream()
								   .map(a -> a.name)
								   .collect(Collectors.joining(", "));
		String conditions = (this.conditions.isEmpty()) ? "" : 
						"WHERE " + this.conditions.stream()
									   .map(cond -> cond.field + " = ?")
									   .collect(Collectors.joining(" AND "));
		String order = (orderBy == null) ? "" : "ORDER BY " + orderBy + ((orderDesc) ? " DESC" : " ASC");
		String limit = (this.limit == 0) ? "" : "LIMIT " + this.limit;
		return "SELECT " + fields 
			 + " FROM " + table 
			 + " " + conditions 
			 + " " + order
			 + " " + limit;
	}
	
	private T buildEntity(ResultSet rs) throws SQLException
	{
		T entity = classType.get();
		for (FieldMapper<?> fieldMapper : fields)
		{
			// Cannot change to stream() because of checked exception handling
			mapFieldToEntity(rs, fieldMapper, entity);
		}
		return entity;
	}
	
	private <F> void mapFieldToEntity(ResultSet rs, FieldMapper<F> mapper, T element) throws SQLException
	{
		F fieldData = null;
		try
		{
			fieldData = rs.getObject(mapper.name, mapper.type);
			
			// No point in setting up null. Set default value instead.
			// This fixes problems when we try to set null for a primitive type
			if (fieldData != null)
			{
				mapper.setter.accept(element, fieldData);
			}
		}
		catch (Exception e)
		{
			String message = "Element of type [" + element.getClass().getSimpleName() + "] has a setter " +
								"[" + mapper.name + "(" + mapper.type + ")] that cannot be set with [" + fieldData + "]";
			throw new RuntimeException(message, e);
		}
	}
	
	private class FieldMapper<O>
	{
		String name;
		Class<O> type;
		BiConsumer<T, O> setter;
	}
	
	private class Condition<V>
	{
		String field;
		V value;
	}
}
