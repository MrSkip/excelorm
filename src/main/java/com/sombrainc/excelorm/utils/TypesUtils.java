package com.sombrainc.excelorm.utils;

public class TypesUtils {

    public static boolean ifTypeIsPureObject(Class<?> type) {
        return Double.class.equals(type)
                || Integer.class.equals(type) || int.class.equals(type)
                || String.class.equals(type);
    }

}
