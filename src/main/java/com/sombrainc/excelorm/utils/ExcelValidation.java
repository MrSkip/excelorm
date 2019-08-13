package com.sombrainc.excelorm.utils;

import org.apache.poi.ss.util.CellRangeAddress;

public class ExcelValidation {

    private ExcelValidation() {
    }

    public static boolean isIteratingOverColumns(CellRangeAddress range) {
        // exclude condition when one cell selected
        return range.getFirstColumn() != range.getLastColumn()
                && range.getFirstRow() == range.getLastRow();
    }

    public static boolean isIteratingOverRows(CellRangeAddress range) {
        // exclude condition when one cell selected
        return range.getFirstColumn() == range.getLastColumn()
                && range.getFirstRow() != range.getLastRow();
    }

    public static boolean isOneCellSelected(CellRangeAddress range) {
        return range.getFirstRow() == range.getLastRow()
                && range.getFirstColumn() == range.getLastColumn();
    }

}
