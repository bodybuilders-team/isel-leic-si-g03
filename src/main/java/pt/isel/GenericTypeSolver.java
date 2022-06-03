package pt.isel;

import java.lang.reflect.ParameterizedType;

// TODO: 03/06/2022 Comment
public class GenericTypeSolver {

    public static Class<?> getTypeArgument(Class<?> clazz) {
        return (Class<?>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
