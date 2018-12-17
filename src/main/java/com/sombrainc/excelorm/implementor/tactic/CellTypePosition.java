package com.sombrainc.excelorm.implementor.tactic;

import com.sombrainc.excelorm.annotation.CellPosition;
import com.sombrainc.excelorm.enumeration.DataQualifier;
import com.sombrainc.excelorm.implementor.CellIndexTracker;
import com.sombrainc.excelorm.utils.ExcelUtils;
import com.sombrainc.excelorm.utils.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.sombrainc.excelorm.utils.ExcelUtils.*;
import static com.sombrainc.excelorm.utils.ReflectionUtils.getClassFromGenericField;
import static com.sombrainc.excelorm.utils.TypesUtils.ifTypeIsPureObject;

public class CellTypePosition<E> extends AbstractTactic<E> implements CellTypeHandler {

    public CellTypePosition(Field field, E instance, Sheet sheet, CellIndexTracker tracker) {
        super(field, instance, sheet, tracker);
    }

    @Override
    public Object process() {
        CellPosition annotation = field.getAnnotation(CellPosition.class);
        CellRangeAddress range = tracker.rearrangeCell(field);

        Object fieldValue = null;
        if (annotation.strategy() == DataQualifier.ROW_UNTIL_NULL
                || annotation.strategy() == DataQualifier.COLUMN_UNTIL_NULL) {
            fieldValue = createListAndIteranteUtilEmpty(range, annotation.strategy());
        } else if (List.class.equals(field.getType())) {
            fieldValue = createListAndIterateOverRange(range);
        } else if (ifTypeIsPureObject(field.getType())) {
            Iterator<CellAddress> iterator = range.iterator();
            fieldValue = readSingleValueFromSheet(field.getType(), getOrCreateCell(sheet, iterator.next()));
        }

        return fieldValue;
    }

    @NotNull
    private List<Object> createListAndIterateOverRange(CellRangeAddress range) {
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
        return list;
    }

    private List<Object> createListAndIteranteUtilEmpty(CellRangeAddress range, DataQualifier qualifier) {
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

}
