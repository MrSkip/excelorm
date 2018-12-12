package com.sombrainc.excelorm.utils;

public class StringUtils {
    public static boolean isNullOrEmpty(Object o) {
        if (o instanceof String) {
            return ((String) o).trim().isEmpty();
        }
        return o == null;
    }
}
