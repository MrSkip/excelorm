package com.sombrainc.excelorm.implementor;

import com.sombrainc.excelorm.annotation.CellMap;
import com.sombrainc.excelorm.annotation.CellPosition;
import com.sombrainc.excelorm.enumeration.DataQualifier;
import com.sombrainc.excelorm.utils.ExcelUtils;
import com.sombrainc.excelorm.utils.StringUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

import static com.sombrainc.excelorm.utils.ExcelUtils.*;

public class ExcelReader {

    public static <E> E read(Sheet sheet, Class<E> target) {
        E instance = getInstance(target);

        Field[] allFields = FieldUtils.getAllFields(target);
        for (Field field : allFields) {
            Object fieldValue = null;
            if (field.isAnnotationPresent(CellPosition.class)) {
                fieldValue = positionTactic(field, instance, sheet);
            } else if (field.isAnnotationPresent(CellMap.class)) {
                fieldValue = mapTactic(field, instance, sheet);
            }

            if (fieldValue != null) {
                field.setAccessible(true);
                try {
                    field.set(instance, fieldValue);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return instance;
    }

    private static <E> Object mapTactic(Field field, E instance, Sheet sheet) {
        if (!Map.class.equals(field.getType())) {
            throw new IllegalStateException("Incorrect field type. Map is required");
        }

        CellMap annotation = field.getAnnotation(CellMap.class);

        CellRangeAddress rangeKey = CellRangeAddress.valueOf(annotation.keyCell());
        CellRangeAddress rangeValue = CellRangeAddress.valueOf(annotation.firstValueCell());
        DataQualifier strategy = annotation.strategy();

        Object fieldValue = null;

        Type[] types = getClassFromGenericField(field);

        if (strategy == DataQualifier.ROW_UNTIL_NULL
                || strategy == DataQualifier.COLUMN_UNTIL_NULL) {
            fieldValue = iterateUntilEmptyCellMap(sheet, rangeKey, rangeValue, strategy, types);
        } else {
            Cell keyCell = createOrGetFirstCell(sheet, rangeKey);
            Object keyCellValue = readStraightTypeFromExcel(keyCell);
            HashMap<Object, Object> map = new HashMap<>();
            if (!StringUtils.isNullOrEmpty(keyCellValue)) {
                Cell valueCell = createOrGetFirstCell(sheet, rangeValue);
                Object valueCellValue = readStraightTypeFromExcel(valueCell);
                map.put(keyCellValue, valueCellValue);
            }
            fieldValue = map;
        }

        return fieldValue;
    }

    private static <E> Object positionTactic(Field field, E instance, Sheet sheet) {
        CellPosition annotation = field.getAnnotation(CellPosition.class);

        CellRangeAddress range = CellRangeAddress.valueOf(annotation.position());
        DataQualifier strategy = annotation.strategy();

        Object fieldValue = null;
        if (strategy == DataQualifier.ROW_UNTIL_NULL
                || strategy == DataQualifier.COLUMN_UNTIL_NULL) {
            fieldValue = iterateUntilEmptyCellList(field, sheet, range, strategy);
        } else if (ifTypeIsPureObject(field.getType())) {
            Iterator<CellAddress> iterator = range.iterator();
            fieldValue = readSingleValueFromSheet(field.getType(), getOrCreateCell(sheet, iterator.next()));
        }

        return fieldValue;
    }

    private static List<Object> iterateUntilEmptyCellList(Field field, Sheet sheet, CellRangeAddress range, DataQualifier qualifier) {
        if (!List.class.equals(field.getType())) {
            throw new IllegalStateException("Incorrect field type. Collection is required");
        }

        int index = DataQualifier.COLUMN_UNTIL_NULL.equals(qualifier)
                ? range.getFirstColumn()
                : range.getFirstRow();

        List<Object> list = new ArrayList<>();
        Class<?> clazz = (Class<?>) getClassFromGenericField(field)[0];

        while (true) {
            Cell cell = DataQualifier.COLUMN_UNTIL_NULL.equals(qualifier)
                    ? ExcelUtils.createOrGetCell(sheet, range.getFirstRow(), index)
                    : ExcelUtils.createOrGetCell(sheet, index, range.getFirstColumn());

            if (StringUtils.isNullOrEmpty(readStraightTypeFromExcel(cell))) {
                break;
            }

            Object cellValue = readSingleValueFromSheet(clazz, cell);
            list.add(cellValue);
            index++;
        }
        return list;
    }

    private static Map<Object, Object> iterateUntilEmptyCellMap(Sheet sheet,
                                                                CellRangeAddress keyRange,
                                                                CellRangeAddress valueRange,
                                                                DataQualifier qualifier,
                                                                Type[] types) {
        int index = DataQualifier.COLUMN_UNTIL_NULL.equals(qualifier)
                ? keyRange.getFirstColumn()
                : keyRange.getFirstRow();

        Class<?> clazzKey = (Class<?>) types[0];
        Class<?> clazzValue = (Class<?>) types[1];

        Map<Object, Object> map = new HashMap<>();
        while (true) {
            Cell keyCell = DataQualifier.COLUMN_UNTIL_NULL.equals(qualifier)
                    ? ExcelUtils.createOrGetCell(sheet, keyRange.getFirstRow(), index)
                    : ExcelUtils.createOrGetCell(sheet, index, keyRange.getFirstColumn());

            if (StringUtils.isNullOrEmpty(readStraightTypeFromExcel(keyCell))) {
                break;
            }

            Cell valueCell = DataQualifier.COLUMN_UNTIL_NULL.equals(qualifier)
                    ? ExcelUtils.createOrGetCell(sheet, valueRange.getFirstRow(), index)
                    : ExcelUtils.createOrGetCell(sheet, index, valueRange.getFirstColumn());

            Object valueInKeyCell = readSingleValueFromSheet(clazzKey, keyCell);
            Object valueInValueCell = readSingleValueFromSheet(clazzValue, valueCell);

            map.put(valueInKeyCell, valueInValueCell);
            index++;
        }

        return map;
    }

    private static Type[] getClassFromGenericField(Field field) {
        Type type = field.getGenericType();
        ParameterizedType pType = (ParameterizedType) type;
        return pType.getActualTypeArguments();
    }

    private static boolean ifTypeIsPureObject(Class<?> type) {
        return Double.class.equals(type)
                || Integer.class.equals(type) || int.class.equals(type)
                || String.class.equals(type);
    }

    private static <E> E getInstance(Class<E> target) {
        E instance;
        try {
            instance = ConstructorUtils.getAccessibleConstructor(target).newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Could not get instance of the class " + target.getCanonicalName());
        }
        return instance;
    }

}
