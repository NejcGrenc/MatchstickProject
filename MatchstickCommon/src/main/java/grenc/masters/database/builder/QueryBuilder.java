package grenc.masters.database.builder;

import java.util.function.Supplier;

public class QueryBuilder
{	
	public static <T> SelectBuilder<T> newSelect(Supplier<T> classType) {
		return new SelectBuilder<T>(classType);
	}

	public static <T> InsertBuilder<T> newInsert() {
		return new InsertBuilder<T>();
	}
	
	public static <T> UpdateBuilder<T> newUpdate() {
		return new UpdateBuilder<T>();
	}

	public static CountBuilder newCount() {
		return new CountBuilder();
	}
}

