package com.sombrainc.excelorm.utils;

import com.sombrainc.excelorm.exception.IncorrectRangeException;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Field;

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

    public static CellDirection validateRange(String range, Field field) {
        if (range == null || range.trim().isEmpty()) {
            throw new IncorrectRangeException(
                    String.format("Specified cell range for field '%s' is empty or null", field.getName())
            );
        }
        String message = String.format("Incorrect range for field '%s' ", field.getName());
        try {
            CellRangeAddress cellAddresses = CellRangeAddress.valueOf(range);
            if (cellAddresses.getFirstColumn() == -1 || cellAddresses.getFirstRow() == -1) {
                throw new IncorrectRangeException(message);
            }
            return new CellDirection(cellAddresses, field);
        } catch (Exception e) {
            throw new IncorrectRangeException(message, e);
        }
    }

    public static class CellDirection {
        private CellRangeAddress range;
        private Field field;

        private CellDirection(CellRangeAddress range, Field field) {
            this.range = range;
            this.field = field;
        }

        public CellRangeAddress straightRange() {
            return range;
        }

        public CellRangeAddress validateAndGetRange() {
            if (isOneCellSelected(range)) {
                return range;
            }
            if (!isIteratingOverColumns(range) && !isIteratingOverRows(range)) {
                throw new IncorrectRangeException(
                        String.format("This field '%s' supports iteration only over columns or rows." +
                                " It means each cell from the selected range must be on the same row or column", field.getName())
                );
            }
            return range;
        }
    }

}
