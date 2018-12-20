package com.sombrainc.excelorm.annotation;

import com.sombrainc.excelorm.enumeration.CellStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CellMap {

    CellStrategy strategy() default CellStrategy.FIXED;

    int step() default 1;

    String keyCell();

    String valueCell() default "";

}
