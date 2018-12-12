package com.sombrainc.excelorm.implementor;

import com.sombrainc.excelorm.annotation.CellMap;
import com.sombrainc.excelorm.annotation.CellPosition;
import com.sombrainc.excelorm.enumeration.DataQualifier;
import com.sombrainc.excelorm.utils.ExcelUtils;
import com.sombrainc.excelorm.utils.StringUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelReader {

    public static <E> E read(Sheet sheet, Class<E> target) {
        E instance = getInstance(target);

        Field[] allFields = FieldUtils.getAllFields(target);
        for (Field field : allFields) {
            if (field.isAnnotationPresent(CellPosition.class)) {
                transferValuesFromExcel(field, instance, sheet);
            } else if (field.isAnnotationPresent(CellMap.class)) {

            }
        }

        return instance;
    }

    private static <E> void transferValuesFromExcel(Field field, E instance, Sheet sheet) {
        CellPosition annotation = field.getAnnotation(CellPosition.class);

        CellRangeAddress range = CellRangeAddress.valueOf(annotation.position());
        DataQualifier strategy = annotation.strategy();

        Object fieldValue = null;
        if (strategy == DataQualifier.ROW_UNTIL_NULL
                || strategy == DataQualifier.COLUMN_UNTIL_NULL) {
            fieldValue = iterateUntilEmptyCell(field, sheet, range, strategy);
        } else if (List.class.equals(field.getType())) {
            List<Object> list = new ArrayList<>();
            Class<?> clazz = getClassFromGenericField(field);
            for (CellAddress cellAddress : range) {
                Cell cell = getOrCreateCell(sheet, cellAddress);
                if (StringUtils.isNullOrEmpty(readStraightTypeFromExcel(cell))) {
                    break;
                }
                Object cellValue = readSingleValueFromSheet(clazz, cell);
                list.add(cellValue);
            }
            fieldValue = list;
        } else if (ifTypeIsPureObject(field.getType())) {
            Iterator<CellAddress> iterator = range.iterator();
            fieldValue = readSingleValueFromSheet(field.getType(), getOrCreateCell(sheet, iterator.next()));
        }

        field.setAccessible(true);
        try {
            field.set(instance, fieldValue);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static Cell getOrCreateCell(Sheet sheet, CellAddress cellAddress) {
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

    private static List<Object> iterateUntilEmptyCell(Field field, Sheet sheet, CellRangeAddress range, DataQualifier qualifier) {
        if (!List.class.equals(field.getType())) {
            throw new IllegalStateException("Incorrect field type. Collection required");
        }

        int index = DataQualifier.COLUMN_UNTIL_NULL.equals(qualifier)
                ? range.getFirstColumn()
                : range.getFirstRow();

        List<Object> list = new ArrayList<>();
        Class<?> clazz = getClassFromGenericField(field);

        while (true) {
            Cell cell = DataQualifier.COLUMN_UNTIL_NULL.equals(qualifier)
                    ? ExcelUtils.createOrGetCell(sheet, range.getFirstRow(), index)
                    : ExcelUtils.createOrGetCell(sheet, index, range.getFirstRow());

            if (StringUtils.isNullOrEmpty(readStraightTypeFromExcel(cell))) {
                break;
            }

            Object cellValue = readSingleValueFromSheet(clazz, cell);
            list.add(cellValue);
            index++;
        }
        return list;
    }

    private static Class<?> getClassFromGenericField(Field field) {
        Type type = field.getGenericType();
        ParameterizedType pType = (ParameterizedType) type;
        return (Class<?>) pType.getActualTypeArguments()[0];
    }

    private static Object readSingleValueFromSheet(Class<?> type, Cell cell) {
        if (Double.class.equals(type)) {
            return cell.getNumericCellValue();
        } else if (Integer.class.equals(type) || int.class.equals(type)) {
            return (int) cell.getNumericCellValue();
        } else if (String.class.equals(type)) {
            return cell.getStringCellValue();
        } else if (Object.class.equals(type)) {
            return readStraightTypeFromExcel(cell);
        } else {
            throw new NullPointerException("There is no valid type handler for " + type);
        }
    }

    private static Object readStraightTypeFromExcel(Cell cell) {
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

    private static boolean ifTypeIsPureObject(Class<?> type) {
        return Double.class.equals(type)
                || Integer.class.equals(type) || int.class.equals(type)
                || String.class.equals(type);
    }

    private static Cell getCell(Sheet sheet, Field field) {
        String position = field.getAnnotation(CellPosition.class).position();
        return ExcelUtils.getCellLocationOnSheet(sheet, position);
    }

    private static <E> E getInstance(Class<E> target) {
        E instance;
        try {
            instance = ConstructorUtils.getAccessibleConstructor(target).newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Could not get instance of class " + target.getCanonicalName());
        }
        return instance;
    }

}
