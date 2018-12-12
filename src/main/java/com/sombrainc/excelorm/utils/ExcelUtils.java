package com.sombrainc.excelorm.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
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

    public static Cell createOrGetCell(Sheet sheet, CellRangeAddress range) {
        return createOrGetCell(sheet, range.getFirstRow(), range.getFirstColumn());
    }

}
