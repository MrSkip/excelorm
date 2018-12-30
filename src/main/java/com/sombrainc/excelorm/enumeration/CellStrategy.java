package com.sombrainc.excelorm.enumeration;

/**
 * Use to specify how the data will be read
 */
public enum CellStrategy {

    /**
     * In other words that's fixed RANGE
     * For example:
     * - A2
     * - A2:A4
     * - A2:C5
     */
    FIXED,

    /**
     * Continue reading cells until first empty/null cell is found
     * In this case iteration goes over rows (row by row if step is not defined)
     */
    COLUMN_UNTIL_NULL,

    /**
     * Continue reading cells until first empty/null cell is found
     * In this case iteration goes over columns (column by column if step is not defined)
     */
    ROW_UNTIL_NULL

}
