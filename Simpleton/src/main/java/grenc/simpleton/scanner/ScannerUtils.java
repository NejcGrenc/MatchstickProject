package grenc.simpleton.scanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class ScannerUtils
{
	/**
	 * Filters only annotated classes.
	 * @param allClasses to be filtered
	 * @param annotation to use for filtering
	 * @return an array of classes that are annotated with specified annotation
	 */
	public static Class<?>[] annotatedClasses(Class<?>[] allClasses, Class<? extends Annotation> annotation)
	{
		return stream(allClasses).filter(c -> c.isAnnotationPresent(annotation)).toArray(Class<?>[]::new);			
	}
	
	/**
	 * Returns all classes, which contain at least one annotated filed with the specified annotation
	 * @param allClasses to be searched
	 * @param annotation to use
	 * @return an array of classes that contain fields annotated with specified annotation
	 */
	public static Class<?>[] containsAnnotatedFields(Class<?>[] allClasses, Class<? extends Annotation> annotation)
	{
		return stream(allClasses).filter(c -> streamAllFieldsAsAccessible(c).anyMatch(f -> f.isAnnotationPresent(annotation))).toArray(Class<?>[]::new);
	}
	
	/**
	 * Returns all fields in provided classes, that are annotated with the specified annotation
	 * @param allClasses to be searched
	 * @param annotation to use
	 * @return an array of fields that are annotated with specified annotation
	 */
	public static Field[] annotatedFields(Class<?>[] allClasses, Class<? extends Annotation> annotation)
	{
		return stream(allClasses).flatMap(c -> stream(annotatedFields(c, annotation))).toArray(Field[]::new);			
	}
	
	/**
	 * Returns all fields in provided class, that are annotated with the specified annotation
	 * @param class to be searched
	 * @param annotation to use
	 * @return an array of fields that are annotated with specified annotation
	 */
	public static Field[] annotatedFields(Class<?> c, Class<? extends Annotation> annotation)
	{
		return streamAllFieldsAsAccessible(c).filter(f -> f.isAnnotationPresent(annotation)).toArray(Field[]::new);			
	}
	
	protected static Field[] getAllFields(Class<?> c)
	{
		try {
			Field[] allClassFields = c.getDeclaredFields();
			if (c.getSuperclass() != null)
				allClassFields = merge(allClassFields, getAllFields(c.getSuperclass()));
			return allClassFields;
		}
		catch (NoClassDefFoundError e)
		{
    		System.out.println("ERROR: " + e.getClass() + ": "+ e.getMessage());
			return new Field[0];
		}
	}
	
	private static <T> Stream<T> stream(T[] array)
	{
		return Arrays.asList(array).stream();
	}
	
	private static Stream<Field> streamAllFieldsAsAccessible(Class<?> c)
	{
		stream(getAllFields(c)).forEach(f -> f.setAccessible(true));
		return stream(getAllFields(c));
	}

	private static Field[] merge(Field[] array1, Field[] array2)
	{
		List<Field> all = new ArrayList<>();
		all.addAll(Arrays.asList(array1));
		all.addAll(Arrays.asList(array2));
		return all.toArray(new Field[0]);
	}
}
