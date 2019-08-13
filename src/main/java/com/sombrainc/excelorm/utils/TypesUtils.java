package com.sombrainc.excelorm.utils;

import java.math.BigDecimal;

public class TypesUtils {

    private TypesUtils() {
    }

    public static boolean isPureObject(Class<?> type) {
        return Double.class.equals(type) || double.class.equals(type)
                || Integer.class.equals(type) || int.class.equals(type)
                || Long.class.equals(type) || long.class.equals(type)
                || Boolean.class.equals(type) || boolean.class.equals(type)
                || Float.class.equals(type) || float.class.equals(type)
                || String.class.equals(type)
                || BigDecimal.class.equals(type)
                || Object.class.equals(type)
                || type.isEnum();
    }

}
