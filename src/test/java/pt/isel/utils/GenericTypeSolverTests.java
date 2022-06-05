package pt.isel.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GenericTypeSolverTests {

    // Created for test purposes
    private abstract static class TestAbstractClass<T> {}

    @Test
    public void getTypeArgumentTestWithInteger() {
        TestAbstractClass<Integer> testAbstractClass = new TestAbstractClass<>() {};
        Class<?> clazz = testAbstractClass.getClass();
        Class<?> typeArgument = GenericTypeSolver.getTypeArgument(clazz);
        assertEquals(typeArgument, Integer.class);
    }

    @Test
    public void getTypeArgumentTestWithString() {
        TestAbstractClass<String> testAbstractClass = new TestAbstractClass<>() {};
        Class<?> clazz = testAbstractClass.getClass();
        Class<?> typeArgument = GenericTypeSolver.getTypeArgument(clazz);
        assertEquals(typeArgument, String.class);
    }

    @Test(expected = ClassCastException.class)
    public void getTypeArgumentTestWithNonAbstractClassThrows() {
        String testNonAbstractClass = "";
        Class<?> clazz = testNonAbstractClass.getClass();
        Class<?> typeArgument = GenericTypeSolver.getTypeArgument(clazz);
    }

}
