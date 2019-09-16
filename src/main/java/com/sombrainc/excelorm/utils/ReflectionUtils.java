package com.sombrainc.excelorm.utils;

import com.sombrainc.excelorm.exception.POIRuntimeException;
import org.apache.commons.lang3.reflect.ConstructorUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReflectionUtils {

    private ReflectionUtils() {
    }

    public static <E> E getInstance(Class<E> target) {
        if (target == null) {
            throw new POIRuntimeException("Target class could not be null");
        }
        E instance;
        try {
            instance = ConstructorUtils.getAccessibleConstructor(target).newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Could not get instance of the class " + target.getCanonicalName());
        }
        return instance;
    }

    public static <E> void setFieldViaReflection(E instance, Field field, Object fieldValue) {
        field.setAccessible(true);
        try {
            field.set(instance, fieldValue);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Type[] getClassFromGenericField(Field field) {
        Type type = field.getGenericType();
        ParameterizedType pType = (ParameterizedType) type;
        return pType.getActualTypeArguments();
    }

}
