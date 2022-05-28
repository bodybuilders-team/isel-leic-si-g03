package pt.isel;

import java.lang.reflect.ParameterizedType;

public class GenericTypeSolver {

    public static Class<?> getTypeArgument(Class<?> clazz) {
        return (Class<?>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
