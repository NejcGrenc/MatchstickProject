package grenc.growscript.service.handler;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import grenc.growscript.service.exception.GrowScriptException;

public class ClassProcessor
{
	public static List<String> allFields(Class<?> c)
	{
		return declaredFields(c).map(field -> field.getName()).collect(Collectors.toList());
	}
	
	public static List<String> allFieldsOfImplementing(Class<?> c, Class<?> fieldType)
	{
		return declaredFields(c)
				.filter(field -> isInterface(field.getType(), fieldType) || implementsInterface(field.getType(), fieldType))
				.map(field -> field.getName()).collect(Collectors.toList());
	}
	
	public static Field findFieldByName(Class<?> c, String fieldName)
	{
		return declaredFields(c).filter(field -> field.getName().equals(fieldName)).findFirst().orElseThrow(() ->
				new GrowScriptException("No field by name [" + fieldName + "] found for class [" + c + "]."));
	}
	
	
	private static Stream<Field> declaredFields(Class<?> c)
	{
		return Arrays.asList(c.getDeclaredFields()).stream().filter(field -> ! isThis(field.getName()));
	}
	
	private static boolean isInterface(Class<?> original, Class<?> selectedInterface)
	{
		return original.equals(selectedInterface);
	}	
	
	private static boolean implementsInterface(Class<?> original, Class<?> selectedInterface)
	{
		return Arrays.asList(original.getInterfaces()).contains(selectedInterface);
	}
	
	private static boolean isThis(String fieldName)
	{
		return fieldName.startsWith("this$");
	}
}
