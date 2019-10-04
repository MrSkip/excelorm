package com.sombrainc.excelorm.utils;

import com.sombrainc.excelorm.exception.POIRuntimeException;

import java.util.Objects;

public class StringUtils {

    private StringUtils() {
    }

    public static boolean isNullOrEmpty(Object o) {
        if (o instanceof String) {
            return ((String) o).trim().isEmpty();
        }
        return o == null;
    }

    public static <T> T requireNotBlank(T t) {
        if (t instanceof String) {
            if (((String) t).trim().isEmpty())
                throw new POIRuntimeException("Not blank is required");
            return t;
        }
        return Objects.requireNonNull(t);
    }
}
