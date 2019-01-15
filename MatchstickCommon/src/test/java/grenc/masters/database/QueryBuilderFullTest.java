package grenc.masters.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import grenc.masters.database.PostgresConnector;
import grenc.masters.database.builder.QueryBuilder;
import grenc.masters.database.test.DatabaseTestConfiguraton;

public class QueryBuilderFullTest extends DatabaseTestConfiguraton
{
	protected PostgresConnector connector = new PostgresConnector();
	
	@Before
	public void setup() throws SQLException, IOException
	{
		Connection connection = connector.open();
		connection.createStatement().execute("insert into test_entity (name, value) values ('demo', 1234);");
		connection.createStatement().execute("insert into test_entity (name, value) values ('test', 5678);");
		connection.createStatement().execute("insert into test_entity (name, value) values ('demo', 5678);");
		connector.close();	
	}


	@Test
	public void selectsFullEntity()
	{
		List<TestEntity> entities = QueryBuilder.newSelect(TestEntity::new)
				.fromTable("test_entity")
				.getField("id", Integer.class, TestEntity::setId)
				.getField("name", String.class, TestEntity::setName)
				.getField("value", Long.class, TestEntity::setValue)
				.execute();
		
		assertNotNull(entities);
		assertFalse(entities.isEmpty());
		assertNotNull(entities.get(0));
		assertNotNull(entities.get(0).getId());
		assertNotNull(entities.get(0).getName());
		assertNotNull(entities.get(0).getValue());
	}
	
	@Test
	public void selectsAppropriateField()
	{
		List<TestEntity> entities = QueryBuilder.newSelect(TestEntity::new)
				.fromTable("test_entity")
				.getField("id", Integer.class, TestEntity::setId)
				.where("id", 1)
				.execute();
		
		assertNotNull(entities);
		assertFalse(entities.isEmpty());
		assertNotNull(entities.get(0));
		assertEquals(1, entities.get(0).getId());
	}
	
	@Test
	public void selectsOnMultipleConditions()
	{
		List<TestEntity> entities = QueryBuilder.newSelect(TestEntity::new)
				.fromTable("test_entity")
				.getField("id", Integer.class, TestEntity::setId)
				.where("name", "demo")
				.where("value", 5678)
				.execute();
		
		assertNotNull(entities);
		assertFalse(entities.isEmpty());
		assertNotNull(entities.get(0));
		assertEquals(3, entities.get(0).getId());
	}
	
	@Test
	public void selectsDescOrder()
	{
		List<TestEntity> entities = QueryBuilder.newSelect(TestEntity::new)
				.fromTable("test_entity")
				.getField("id", Integer.class, TestEntity::setId)
				.orderByDesc("id", true)
				.execute();
		
		assertNotNull(entities);
		assertFalse(entities.isEmpty());
		assertNotNull(entities.get(0));
		assertNotNull(entities.get(1));
		assertTrue(entities.get(0).getId() > entities.get(1).getId());
	}
	
	@Test
	public void selectsAscOrder()
	{
		List<TestEntity> entities = QueryBuilder.newSelect(TestEntity::new)
				.fromTable("test_entity")
				.getField("id", Integer.class, TestEntity::setId)
				.orderByDesc("id", false)
				.execute();
		
		assertNotNull(entities);
		assertFalse(entities.isEmpty());
		assertNotNull(entities.get(0));
		assertNotNull(entities.get(1));
		assertTrue(entities.get(0).getId() < entities.get(1).getId());
	}
	
	@Test
	public void selectAppropriateLimit()
	{
		List<TestEntity> entities = QueryBuilder.newSelect(TestEntity::new)
				.fromTable("test_entity")
				.getField("id", Integer.class, TestEntity::setId)
				.limit(2)
				.execute();
		
		assertNotNull(entities);
		assertEquals(2, entities.size());
	}
	
	
	@Test
	public void insertsEntity()
	{
		List<TestEntity> initial = getAll();
		
		QueryBuilder.newInsert().intoTable("test_entity")
					.setField("name", "inserted")
					.setField("value", 444555)
					.execute();
		
		List<TestEntity> after = getAll();

		assertEquals(initial.size() + 1, after.size());
		assertEquals(after.get(0).getName(), "inserted");
		assertEquals(after.get(0).getValue(), 444555);
	}
	
	private List<TestEntity> getAll()
	{
		return QueryBuilder.newSelect(TestEntity::new)
				.fromTable("test_entity")
				.getField("id", Integer.class, TestEntity::setId)
				.getField("name", String.class, TestEntity::setName)
				.getField("value", Long.class, TestEntity::setValue)
				.orderByDesc("id", true)
				.execute();
	}
	

	@Test
	public void updatesEntity()
	{
		List<TestEntity> initial = QueryBuilder.newSelect(TestEntity::new)
				.fromTable("test_entity")
				.getField("id", Integer.class, TestEntity::setId)
				.getField("name", String.class, TestEntity::setName)
				.getField("value", Long.class, TestEntity::setValue)
				.where("name", "test")
				.execute();
		
		QueryBuilder.newUpdate().inTable("test_entity")
					.setField("name", "inserted")
					.setCondition("name", "test")
					.execute();
		
		List<TestEntity> after = QueryBuilder.newSelect(TestEntity::new)
				.fromTable("test_entity")
				.getField("id", Integer.class, TestEntity::setId)
				.getField("name", String.class, TestEntity::setName)
				.getField("value", Long.class, TestEntity::setValue)
				.where("name", "inserted")
				.execute();

		assertEquals(initial.size(), after.size());
		assertEquals(initial.get(0).getName(), "test");
		assertEquals(after.get(0).getName(), "inserted");
		assertEquals(initial.get(0).getId(), initial.get(0).getId());
	}
	
}
