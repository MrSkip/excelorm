package com.sombrainc.excelorm.implementor.tactic;

import com.sombrainc.excelorm.annotation.CellMap;
import com.sombrainc.excelorm.enumeration.DataQualifier;
import com.sombrainc.excelorm.implementor.CellIndexTracker;
import com.sombrainc.excelorm.utils.ExcelUtils;
import com.sombrainc.excelorm.utils.StringUtils;
import javafx.util.Pair;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static com.sombrainc.excelorm.implementor.ExcelReader.read;
import static com.sombrainc.excelorm.utils.ExcelUtils.readSingleValueFromSheet;
import static com.sombrainc.excelorm.utils.ExcelUtils.readStraightTypeFromExcel;
import static com.sombrainc.excelorm.utils.ReflectionUtils.getClassFromGenericField;
import static com.sombrainc.excelorm.utils.TypesUtils.ifTypeIsPureObject;

public class CellTypeMap<E> extends AbstractTactic<E> implements CellTypeHandler {

    public CellTypeMap(Field field, E instance, Sheet sheet, CellIndexTracker tracker) {
        super(field, instance, sheet, tracker);
    }

    @Override
    public Object process() {
        if (!Map.class.equals(field.getType())) {
            throw new IllegalStateException("Incorrect field type. Map is required");
        }

        CellMap annotation = field.getAnnotation(CellMap.class);
        Pair<CellRangeAddress, CellRangeAddress> pair = tracker.rearrangeMap(field);

        Object fieldValue;
        Type[] types = getClassFromGenericField(field);

        if (annotation.strategy() == DataQualifier.ROW_UNTIL_NULL
                || annotation.strategy() == DataQualifier.COLUMN_UNTIL_NULL) {
            fieldValue = createMapIterateUtilEmptyCell(pair, annotation.strategy(), types);
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

    private Map<Object, Object> createMapIterateUtilEmptyCell(Pair<CellRangeAddress, CellRangeAddress> pair,
                                                              DataQualifier qualifier,
                                                              Type[] types) {
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
                Object nestedObject = read(
                        sheet, clazzValue, new CellIndexTracker(simpleCounter, qualifier)
                );
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

}
