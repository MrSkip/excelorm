package com.sombrainc.excelorm.implementor;

import com.sombrainc.excelorm.annotation.CellMap;
import com.sombrainc.excelorm.annotation.CellPosition;
import com.sombrainc.excelorm.enumeration.DataQualifier;
import com.sombrainc.excelorm.utils.ExcelUtils;
import com.sombrainc.excelorm.utils.StringUtils;
import javafx.util.Pair;
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
        CellIndexTracker tracker = new CellIndexTracker();
        return read(sheet, target, tracker);
    }

    private static <E> E read(Sheet sheet, Class<E> target, CellIndexTracker tracker) {
        E instance = getInstance(target);

        Field[] allFields = FieldUtils.getAllFields(target);
        for (Field field : allFields) {
            Object fieldValue = null;
            if (field.isAnnotationPresent(CellPosition.class)) {
                fieldValue = positionTactic(field, instance, sheet, tracker);
            } else if (field.isAnnotationPresent(CellMap.class)) {
                fieldValue = mapTactic(field, instance, sheet, tracker);
            }

            if (fieldValue != null) {
                setFieldViaReflection(instance, field, fieldValue);
            }
        }

        return instance;
    }

    private static <E> void setFieldViaReflection(E instance, Field field, Object fieldValue) {
        field.setAccessible(true);
        try {
            field.set(instance, fieldValue);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static <E> Object mapTactic(Field field, E instance, Sheet sheet, CellIndexTracker tracker) {
        if (!Map.class.equals(field.getType())) {
            throw new IllegalStateException("Incorrect field type. Map is required");
        }

        CellMap annotation = field.getAnnotation(CellMap.class);
        DataQualifier strategy = annotation.strategy();
        Pair<CellRangeAddress, CellRangeAddress> pair = tracker.rearrangeMap(field);

        Object fieldValue;
        Type[] types = getClassFromGenericField(field);

        if (strategy == DataQualifier.ROW_UNTIL_NULL
                || strategy == DataQualifier.COLUMN_UNTIL_NULL) {
            fieldValue = iterateUntilEmptyCellMap(sheet, pair, strategy, types, tracker);
        } else {
            HashMap<Object, Object> map = new HashMap<>();
            for (CellAddress cellKey : pair.getKey()) {
                // for fixed range A11:A12
            }
//            Cell keyCell = createOrGetFirstCell(sheet, rangeKey);
//            Object keyCellValue = readStraightTypeFromExcel(keyCell);
//
//            if (!StringUtils.isNullOrEmpty(keyCellValue)) {
//                Cell valueCell = createOrGetFirstCell(sheet, rangeValue);
//                Object valueCellValue = readStraightTypeFromExcel(valueCell);
//                map.put(keyCellValue, valueCellValue);
//            }
            fieldValue = map;
        }

        return fieldValue;
    }

    private static <E> Object positionTactic(Field field, E instance, Sheet sheet, CellIndexTracker tracker) {
        CellPosition annotation = field.getAnnotation(CellPosition.class);

        CellRangeAddress range = tracker.rearrangeCell(field);
        DataQualifier strategy = annotation.strategy();

        Object fieldValue = null;
        if (strategy == DataQualifier.ROW_UNTIL_NULL
                || strategy == DataQualifier.COLUMN_UNTIL_NULL) {
            fieldValue = iterateUntilEmptyCellList(field, sheet, range, strategy);
        } else if (List.class.equals(field.getType())) {
            // when just cells range is specified
            List<Object> list = new ArrayList<>();
            Class<?> clazz = (Class<?>) getClassFromGenericField(field)[0];
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
                                                                Pair<CellRangeAddress, CellRangeAddress> pair,
                                                                DataQualifier qualifier,
                                                                Type[] types,
                                                                CellIndexTracker tracker) {
        int index = DataQualifier.COLUMN_UNTIL_NULL.equals(qualifier)
                ? pair.getKey().getFirstColumn()
                : pair.getKey().getFirstRow();

        Class<?> clazzKey = (Class<?>) types[0];
        Class<?> clazzValue = (Class<?>) types[1];

        Map<Object, Object> map = new HashMap<>();

        int simpleCounter = 0;
        while (true) {
            Cell keyCell = DataQualifier.COLUMN_UNTIL_NULL.equals(qualifier)
                    ? ExcelUtils.createOrGetCell(sheet, pair.getKey().getFirstRow(), index)
                    : ExcelUtils.createOrGetCell(sheet, index, pair.getKey().getFirstColumn());

            if (StringUtils.isNullOrEmpty(readStraightTypeFromExcel(keyCell))) {
                break;
            }

            Object valueInKeyCell = readSingleValueFromSheet(clazzKey, keyCell);

            // if object is composite then go ever all its fields
            if (!ifTypeIsPureObject(clazzValue)) {
                Object nestedObject = read(sheet, clazzValue, new CellIndexTracker(simpleCounter));
                map.put(valueInKeyCell, nestedObject);
            } else {
                Cell valueCell = DataQualifier.COLUMN_UNTIL_NULL.equals(qualifier)
                        ? ExcelUtils.createOrGetCell(sheet, pair.getValue().getFirstRow(), index)
                        : ExcelUtils.createOrGetCell(sheet, index, pair.getValue().getFirstColumn());
                Object valueInValueCell = readSingleValueFromSheet(clazzValue, valueCell);
                map.put(valueInKeyCell, valueInValueCell);
            }

            index++;
            simpleCounter++;
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
