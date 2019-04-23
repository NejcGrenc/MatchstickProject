package grenc.masters.simplebean.scanner;

import static org.junit.Assert.assertTrue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Arrays;

import org.junit.Test;

public class ScannerUtilsTest
{
	private Class<?>[] allClasses = new Class<?>[] {TestClass1.class, TestClass2.class, TestClass3.class, TestClass4.class};

	@Test
	public void shouldFilterAnnotatedClasses()
	{
		Class<?>[] result = ScannerUtils.annotatedClasses(allClasses, ClassAnnotation.class);
		assertContains(result, TestClass1.class);
		assertContains(result, TestClass2.class);
		assertNotContains(result, TestClass3.class);
		assertNotContains(result, TestClass4.class);
	}

	@Test
	public void shouldFilterClassesWithAnnotatedFeilds()
	{
		Class<?>[] result = ScannerUtils.containsAnnotatedFields(allClasses, FieldAnnotation.class);
		assertContains(result, TestClass1.class);
		assertNotContains(result, TestClass2.class);
		assertContains(result, TestClass3.class);
		assertNotContains(result, TestClass4.class);
	}
	
	@Test
	public void shouldFilterAnnotatedFields() throws NoSuchFieldException, SecurityException
	{
		Field[] result = ScannerUtils.annotatedFields(allClasses, FieldAnnotation.class);
		assertContains(result, TestClass1.class.getDeclaredField("i"));
		assertContains(result, TestClass3.class.getDeclaredField("i"));
	}
	
	@Test
	public void shouldFilterAnnotatedFieldsForSingle() throws NoSuchFieldException, SecurityException
	{
		Field[] result = ScannerUtils.annotatedFields(TestClass1.class, FieldAnnotation.class);
		assertContains(result, TestClass1.class.getDeclaredField("i"));
		assertNotContains(result, TestClass1.class.getDeclaredField("j"));
		assertNotContains(result, TestClass1.class.getDeclaredField("k"));
	}


	
	private <T> void assertContains(T[] array, T obj)
	{
		assertTrue(Arrays.asList(array).stream().anyMatch(o -> o.equals(obj)));
	}
	private <T> void assertNotContains(T[] array, T obj)
	{
		assertTrue(Arrays.asList(array).stream().noneMatch(o -> o.equals(obj)));
	}
}

@ClassAnnotation
class TestClass1 
{
	@FieldAnnotation
	int i;
	int j;
	@FieldAnnotationBad
	int k;
}

@ClassAnnotation
class TestClass2 
{
	@FieldAnnotationBad
	int i;
}

class TestClass3
{
	@FieldAnnotation
	int i;
}

@ClassAnnotationBad
class TestClass4
{
	int i;
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface ClassAnnotation {}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface FieldAnnotation {}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface ClassAnnotationBad {}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface FieldAnnotationBad {}

