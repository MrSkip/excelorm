package com.sombrainc.excelorm.implementor.tactic.implementation;

import com.sombrainc.excelorm.annotation.CellMap;
import com.sombrainc.excelorm.enumeration.CellStrategy;
import com.sombrainc.excelorm.implementor.CellIndexTracker;
import com.sombrainc.excelorm.implementor.tactic.AbstractTactic;
import com.sombrainc.excelorm.implementor.tactic.CellTypeHandler;
import com.sombrainc.excelorm.model.CellMapPresenter;
import com.sombrainc.excelorm.utils.ExcelUtils;
import com.sombrainc.excelorm.utils.StringUtils;
import com.sombrainc.excelorm.utils.TypesUtils;
import javafx.util.Pair;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static com.sombrainc.excelorm.ExcelReader.read;
import static com.sombrainc.excelorm.utils.ExcelUtils.*;
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

        Object fieldValue;
        Type[] types = getClassFromGenericField(field);
        Pair<CellRangeAddress, CellRangeAddress> pair = rearrangeForMap((Class<?>) types[1]);

        if (annotation.strategy() == CellStrategy.ROW_UNTIL_NULL
                || annotation.strategy() == CellStrategy.COLUMN_UNTIL_NULL) {
            fieldValue = createMapIterateUtilEmptyCell(pair, annotation.strategy(), types, annotation);
        } else {
            fieldValue = createMapIterateOverFixedRange(pair, annotation.strategy(), types, annotation);
        }

        return fieldValue;
    }

    private Map<Object, Object> createMapIterateOverFixedRange(
            Pair<CellRangeAddress, CellRangeAddress> pair, CellStrategy strategy, Type[] types, CellMap annotation) {
        Class<?> clazzKey = (Class<?>) types[0];
        Class<?> clazzValue = (Class<?>) types[1];

        Map<Object, Object> map = new HashMap<>();

        int simpleCounter = 0;
        for (CellAddress keyCellAddress : pair.getKey()) {
            final Cell keyCell = getOrCreateCell(sheet, keyCellAddress);

            if (StringUtils.isNullOrEmpty(readStraightTypeFromExcel(keyCell))) {
                break;
            }

            Object valueInKeyCell = readSingleValueFromSheet(clazzKey, keyCell);

            // if object is composite then go ever all its fields
            if (!ifTypeIsPureObject(clazzValue)) {
                Object nestedObject = read(
                        sheet, clazzValue, new CellIndexTracker(simpleCounter, strategy)
                );
                map.put(valueInKeyCell, nestedObject);
            } else {
                Cell valueCell = ExcelUtils.createOrGetCell(
                        sheet, pair.getValue().getFirstRow() + simpleCounter, pair.getValue().getFirstColumn()
                );

                Object valueInValueCell = readSingleValueFromSheet(clazzValue, valueCell);
                map.put(valueInKeyCell, valueInValueCell);
            }

            simpleCounter += annotation.step();
        }

        return map;
    }

    private Map<Object, Object> createMapIterateUtilEmptyCell(Pair<CellRangeAddress, CellRangeAddress> pair,
                                                              CellStrategy qualifier,
                                                              Type[] types, CellMap annotation) {
        int index = CellStrategy.COLUMN_UNTIL_NULL.equals(qualifier)
                ? pair.getKey().getFirstColumn()
                : pair.getKey().getFirstRow();

        Class<?> clazzKey = (Class<?>) types[0];
        Class<?> clazzValue = (Class<?>) types[1];

        Map<Object, Object> map = new HashMap<>();

        int simpleCounter = 0;
        while (true) {
            Cell keyCell = CellStrategy.COLUMN_UNTIL_NULL.equals(qualifier)
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
                Cell valueCell = CellStrategy.COLUMN_UNTIL_NULL.equals(qualifier)
                        ? ExcelUtils.createOrGetCell(sheet, pair.getValue().getFirstRow(), index)
                        : ExcelUtils.createOrGetCell(sheet, index, pair.getValue().getFirstColumn());
                Object valueInValueCell = readSingleValueFromSheet(clazzValue, valueCell);
                map.put(valueInKeyCell, valueInValueCell);
            }

            index += annotation.step();
            simpleCounter += annotation.step();
        }

        return map;
    }

    private Pair<CellRangeAddress, CellRangeAddress> rearrangeForMap(Class<?> valueType) {
        CellMapPresenter presenter = new CellMapPresenter(field);
        CellStrategy strategy = presenter.getAnnotation().strategy();

        CellRangeAddress keyRange = arrangeCell(strategy, presenter.getKeyRange());
        CellRangeAddress valueRange = calculateRangeForValues(valueType, presenter, strategy, keyRange);

        return new Pair<>(keyRange, valueRange);
    }

    private CellRangeAddress calculateRangeForValues(Class<?> valueType, CellMapPresenter presenter,
                                                     CellStrategy strategy, CellRangeAddress keyRange) {
        if (presenter.getValueRange() != null) {
            return arrangeCell(strategy, presenter.getValueRange());
        } else if (TypesUtils.ifTypeIsPureObject(valueType)) {
            switch (strategy) {
                case FIXED:
                case ROW_UNTIL_NULL:
                    return new CellRangeAddress(
                            keyRange.getFirstRow(), keyRange.getLastRow(),
                            keyRange.getFirstColumn() + 1, keyRange.getLastColumn() + 1
                    );
                case COLUMN_UNTIL_NULL:
                    return new CellRangeAddress(
                            keyRange.getFirstRow() + 1, keyRange.getLastRow() + 1,
                            keyRange.getFirstColumn(), keyRange.getLastColumn()
                    );
            }
        }
        return null;
    }

}
