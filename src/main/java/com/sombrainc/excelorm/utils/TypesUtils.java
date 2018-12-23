package com.sombrainc.excelorm.utils;

import java.math.BigDecimal;

public class TypesUtils {

    public static boolean ifTypeIsPureObject(Class<?> type) {
        return Double.class.equals(type) || double.class.equals(type)
                || Integer.class.equals(type) || int.class.equals(type)
                || Boolean.class.equals(type) || boolean.class.equals(type)
                || Float.class.equals(type) || float.class.equals(type)
                || String.class.equals(type)
                || BigDecimal.class.equals(type)
                || type.isEnum();
    }

}
