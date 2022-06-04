package pt.isel.utils;

import java.lang.reflect.ParameterizedType;


/**
 * Generic type solver.
 */
public class GenericTypeSolver {

    /**
     * Gets the generic type of the first parameter of the given abstract class.
     *
     * @param clazz the abstract class
     * @return the generic type of the first parameter of the given abstract class
     */
    public static Class<?> getTypeArgument(Class<?> clazz) {
        return (Class<?>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
