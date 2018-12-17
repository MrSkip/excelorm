package com.sombrainc.excelorm.annotation;

import com.sombrainc.excelorm.enumeration.DataQualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CellStrategy {

    DataQualifier strategy() default DataQualifier.FIXED;

}
