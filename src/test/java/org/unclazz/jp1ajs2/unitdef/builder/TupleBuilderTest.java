package org.unclazz.jp1ajs2.unitdef.builder;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.unclazz.jp1ajs2.unitdef.Tuple;

public class TupleBuilderTest {

	@Rule
	public final ExpectedException expected = ExpectedException.none();
	
	@Test
	public void add_whenArg0IsNull_throwsException() {
		// Arrange
		final TupleBuilder b = Builders.tuple();
		expected.expect(NullPointerException.class);
		
		// Act
		b.add(null);
		
		// Assert
	}
	
	@Test
	public void add_whenArg0IsNullAndArg1IsNotNull_throwsException() {
		// Arrange
		final TupleBuilder b = Builders.tuple();
		expected.expect(NullPointerException.class);
		
		// Act
		b.add(null, "foo");
		
		// Assert
	}
	
	@Test
	public void add_whenArg0IsNotNullAndArg1IsNull_throwsException() {
		// Arrange
		final TupleBuilder b = Builders.tuple();
		expected.expect(NullPointerException.class);
		
		// Act
		b.add("foo", null);
		
		// Assert
	}
	
	@Test
	public void add_whenArg0IsNotNullAndArg1IsEmpty_doesNotThrowException() {
		// Arrange
		final TupleBuilder b = Builders.tuple();
		
		// Act
		b.add("foo", "");
		final Tuple t = b.build();
		
		// Assert
		assertThat(t.get("foo").toString(), equalTo(""));
	}
	
	@Test
	public void add_whenArg0AndArg1IsNotNull_doesNotThrowException() {
		// Arrange
		final TupleBuilder b = Builders.tuple();
		
		// Act
		b.add("foo", "bar");
		final Tuple t = b.build();
		
		// Assert
		assertThat(t.toString(), equalTo("(foo=bar)"));
	}
}
