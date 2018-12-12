package com.sombrainc.excelorm.annotation;

import com.sombrainc.excelorm.enumeration.CellTarget;
import com.sombrainc.excelorm.enumeration.DataQualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CellMap {

    DataQualifier strategy() default DataQualifier.FIXED;

//    int step() default 1;

    String keyCell();

    String valueCell() default "";

}
