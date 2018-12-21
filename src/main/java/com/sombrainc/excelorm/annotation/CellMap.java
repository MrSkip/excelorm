package com.sombrainc.excelorm.annotation;

import com.sombrainc.excelorm.enumeration.CellStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use to construct the Map based on HashMap
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CellMap {

    /**
     * To determinate the strategy.
     * <p>
     * FIXED - fixed range of single row or column.
     * For example: (A1 - A10 /single column, A1 - D1 /single row, A1 - D4 / WRONG!)
     * <p>
     * ROW_UNTIL_NULL - iterate over rows until first empty/null cell is found
     * (focus only on key cells while iterating)
     * <p>
     * COLUMN_UNTIL_NULL - iterate over columns until first empty/null cell is found
     * (focus only on key cells while iterating)
     *
     * @return strategy
     */
    CellStrategy strategy() default CellStrategy.FIXED;

    /**
     * To define the step while iterating over rows/columns
     *
     * @return step
     */
    int step() default 1;

    /**
     * Use to specify the start of key cells
     *
     * @return position of key cell (range)
     */
    String keyCell();

    /**
     * Use the specify the start of value cell
     *
     * @return value cell
     */
    String valueCell() default "";

}
