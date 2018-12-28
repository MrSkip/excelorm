package com.sombrainc.excelorm.utils;

import com.sombrainc.excelorm.exception.IncorectRangeException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;

import java.math.BigDecimal;

public class ExcelUtils {

    public static CellRangeAddress validateRange(String range, String message) {
        if (range == null || range.trim().isEmpty()) {
            throw new IncorectRangeException(message
                    + ". Specified cell range is empty or null");
        }
        return CellRangeAddress.valueOf(range);
    }

    public static boolean isIteratingOverColumns(CellRangeAddress range) {
        // exclude condition when one cell selected
        return range.getFirstColumn() != range.getLastColumn()
                && range.getFirstRow() == range.getLastRow();
    }

    public static Object readSingleValueFromSheet(Class<?> type, Cell cell) {
        if (Double.class.equals(type) || double.class.equals(type)) {
            return cell.getNumericCellValue();
        } else if (Integer.class.equals(type) || int.class.equals(type)) {
            return (int) cell.getNumericCellValue();
        } else if (String.class.equals(type)) {
            return cell.getStringCellValue();
        } else if (Boolean.class.equals(type) || boolean.class.equals(type)) {
            return cell.getBooleanCellValue();
        } else if (BigDecimal.class.equals(type)) {
            return BigDecimal.valueOf(cell.getNumericCellValue());
        } else if (Float.class.equals(type) || float.class.equals(type)) {
            return (float) cell.getNumericCellValue();
        } else if (type.isEnum()) {
            return valueForEnum(type, cell);
        } else if (Object.class.equals(type)) {
            return readStraightTypeFromExcel(cell);
        } else {
            throw new NullPointerException("There is no valid type handler for " + type);
        }
    }

    private static Object valueForEnum(Class<?> type, Cell cell) {
        Object cellValue = readStraightTypeFromExcel(cell);
        for (Object enumConstant : type.getEnumConstants()) {
            if ((enumConstant + "").trim().equalsIgnoreCase(cellValue + "")) {
                return enumConstant;
            }
        }
        return null;
    }

    public static Object readStraightTypeFromExcel(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case NUMERIC:
                return cell.getNumericCellValue();
            case FORMULA:
                switch (cell.getCachedFormulaResultType()) {
                    case NUMERIC:
                        return cell.getNumericCellValue();
                    case STRING:
                        return cell.getRichStringCellValue();
                    case BOOLEAN:
                        return cell.getBooleanCellValue();
                    default:
                        return "";
                }
            default:
                return "";
        }
    }

    public static Cell getOrCreateCell(Sheet sheet, CellAddress cellAddress) {
        Row row = sheet.getRow(cellAddress.getRow());
        if (row == null) {
            row = sheet.createRow(cellAddress.getRow());
        }
        Cell cell = row.getCell(cellAddress.getColumn());
        if (cell == null) {
            cell = row.createCell(cellAddress.getColumn());
        }
        return cell;
    }

    public static Cell createOrGetCell(Sheet sheet, int rowIndex, int columnIndex) {
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }
        Cell cell = row.getCell(columnIndex);
        if (cell == null) {
            cell = row.createCell(columnIndex);
        }
        return cell;
    }

    public static boolean isOneCellSelected(CellRangeAddress range) {
        return range.getFirstRow() == range.getLastRow()
                && range.getFirstColumn() == range.getLastColumn();
    }

}
