package com.sombrainc.excelorm.utils;

import com.sombrainc.excelorm.exception.HasNotImplementedYetException;
import com.sombrainc.excelorm.exception.IncorrectRangeException;
import com.sombrainc.excelorm.model.pojo.CellDirection;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import static com.sombrainc.excelorm.constants.CommonConstants.DATA_FORMATTER;

public class ExcelUtils {

    private ExcelUtils() {
    }

    public static Object readSingleValueFromSheet(Class<?> type, Cell cell) {
        if (Double.class.equals(type) || double.class.equals(type)) {
            return cell.getNumericCellValue();
        } else if (Integer.class.equals(type) || int.class.equals(type)) {
            return (int) cell.getNumericCellValue();
        } else if (Long.class.equals(type) || long.class.equals(type)) {
            return (long) cell.getNumericCellValue();
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

    // todo handler primitive independently
    public static Object parseToRequiredObject(Class<?> type, Cell cell, FormulaEvaluator evaluator) {
        String str = DATA_FORMATTER.formatCellValue(cell, evaluator);
        if (Double.class.equals(type) || double.class.equals(type)) {
            return Double.parseDouble(str);
        } else if (Integer.class.equals(type) || int.class.equals(type)) {
            return Integer.parseInt(str);
        } else if (Long.class.equals(type) || long.class.equals(type)) {
            return Long.parseLong(str);
        } else if (String.class.equals(type)) {
            return str;
        } else if (Boolean.class.equals(type) || boolean.class.equals(type)) {
            return Boolean.parseBoolean(str);
        } else if (BigDecimal.class.equals(type)) {
            return new BigDecimal(str);
        } else if (Float.class.equals(type) || float.class.equals(type)) {
            return Float.parseFloat(str);
        } else if (type.isEnum()) {
            return valueForEnum(type, cell);
        } else if (Object.class.equals(type)) {
            return readStraightTypeFromExcel(cell);
        } else {
            throw new HasNotImplementedYetException("There is no valid type handler for " + type);
        }
    }

    public static <T> T readGenericValueFromSheet(Class<T> type, Cell cell, FormulaEvaluator formulaEvaluator) {
        return (T) parseToRequiredObject(type, cell, formulaEvaluator);
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

    public static Cell getOrCreateCell(Sheet sheet, int rowIndex, int columnIndex) {
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

    public static CellDirection obtainRange(String range, Field field) {
        String message = String.format("Incorrect range for field '%s' ", field.getName());
        if (range == null || range.trim().isEmpty()) {
            throw new IncorrectRangeException(
                    String.format("Specified cell range for field '%s' is empty or null", field.getName())
            );
        }
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

    public static CellRangeAddress obtainRange(String range) {
        String message = String.format("Incorrect range. Found: %s", range);
        if (range == null || range.trim().isEmpty()) {
            throw new IncorrectRangeException(
                    "Specified range is empty or null"
            );
        }
        try {
            CellRangeAddress cellAddresses = CellRangeAddress.valueOf(range);
            if (cellAddresses.getFirstColumn() == -1 || cellAddresses.getFirstRow() == -1) {
                throw new IncorrectRangeException(message);
            }
            return cellAddresses;
        } catch (Exception e) {
            throw new IncorrectRangeException(message, e);
        }
    }
}
