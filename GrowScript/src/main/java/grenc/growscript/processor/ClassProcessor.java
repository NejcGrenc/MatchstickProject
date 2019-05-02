package grenc.growscript.processor;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClassProcessor
{
	public static List<String> allFields(Class<?> c)
	{
		return fields(c).map(field -> field.getName()).collect(Collectors.toList());
	}
	
	public static List<String> allFieldsOfType(Class<?> c, Class<?> fieldType)
	{
		return fields(c).filter(field -> field.getType().equals(fieldType)).map(field -> field.getName()).collect(Collectors.toList());
	}
	
	private static Stream<Field> fields(Class<?> c)
	{
		return Arrays.asList(c.getDeclaredFields()).stream();
	}
}
