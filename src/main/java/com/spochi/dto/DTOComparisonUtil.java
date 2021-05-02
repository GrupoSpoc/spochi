package com.spochi.dto;

public class DTOComparisonUtil {
    /**
     * Devuelve true si los dos objetos NO son nulos y son iguales
     * o si los dos objetos son nulos
     */
    public static <T> boolean nullOrEquals(T t1, T t2) {
        if (t1 != null && t2 != null) {
            return t1.equals(t2);
        } else {
            return t1 == null && t2 == null;
        }
    }
}
