package com.sombrainc.excelorm.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;

public class ExcelUtils {

    public static Cell getCellLocationOnSheet(Sheet sheet, String position) {
        StringBuilder row = new StringBuilder();
        StringBuilder column = new StringBuilder();

        for (char c : position.trim().toCharArray()) {
            if (Character.isDigit(c)) {
                row.append(c);
            } else {
                column.append(c);
            }
        }

        int rowIndex = Integer.parseInt(row.toString());
        int columnIndex = CellReference.convertColStringToIndex(column.toString());

        return sheet.getRow(rowIndex - 1).getCell(columnIndex);
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
        } else if (Object.class.equals(type)) {
            return readStraightTypeFromExcel(cell);
        } else {
            throw new NullPointerException("There is no valid type handler for " + type);
        }
    }

    public static Object readStraightTypeFromExcel(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case NUMERIC:
                return cell.getNumericCellValue();
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

    public static Cell createOrGetFirstCell(Sheet sheet, CellRangeAddress range) {
        return createOrGetCell(sheet, range.getFirstRow(), range.getFirstColumn());
    }

    public static boolean isOneCellSelected(CellRangeAddress range) {
        return range.getFirstRow() == range.getLastRow()
                && range.getFirstColumn() == range.getLastColumn();
    }

}
