package com.sombrainc.excelorm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use to mark field to get direct value from the Sheet.
 *
 * Works with following objects:
 *
 *  String, Double (double), Integer (int), Double (double),
 *  BigDecimal, Boolean (boolean), Float (float),
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Cell {

    /**
     * To specify a cell position on Sheet
     *
     * @return position of cell
     */
    String cell();
}
