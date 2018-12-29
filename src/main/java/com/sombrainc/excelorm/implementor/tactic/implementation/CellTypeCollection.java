package com.sombrainc.excelorm.implementor.tactic.implementation;

import com.sombrainc.excelorm.annotation.CellCollection;
import com.sombrainc.excelorm.enumeration.CellStrategy;
import com.sombrainc.excelorm.exception.HasNotImplementedYetException;
import com.sombrainc.excelorm.exception.MissingAnnotationException;
import com.sombrainc.excelorm.exception.TypeIsNotSupportedException;
import com.sombrainc.excelorm.implementor.CellIndexTracker;
import com.sombrainc.excelorm.implementor.tactic.AbstractTactic;
import com.sombrainc.excelorm.implementor.tactic.CellTypeHandler;
import com.sombrainc.excelorm.utils.ExcelUtils;
import com.sombrainc.excelorm.utils.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Field;
import java.util.*;

import static com.sombrainc.excelorm.Excelorm.read;
import static com.sombrainc.excelorm.utils.ExcelUtils.*;
import static com.sombrainc.excelorm.utils.ReflectionUtils.getClassFromGenericField;
import static com.sombrainc.excelorm.utils.TypesUtils.ifTypeIsPureObject;

public class CellTypeCollection<E> extends AbstractTactic<E> implements CellTypeHandler {

    public CellTypeCollection(Field field, E instance, Sheet sheet, CellIndexTracker tracker) {
        super(field, instance, sheet, tracker);
    }

    @Override
    public Object process() {
        validate();

        CellCollection annotation = field.getAnnotation(CellCollection.class);
        CellRangeAddress range = rearrangeCell();

        Object fieldValue = null;
        if (annotation.strategy() == CellStrategy.ROW_UNTIL_NULL
                || annotation.strategy() == CellStrategy.COLUMN_UNTIL_NULL) {
            fieldValue = createListAndIterateUtilEmpty(range, annotation);
        } else if (List.class.equals(field.getType())) {
            fieldValue = new ArrayList<>(createListAndIterateOverRange(range, annotation));
        } else if (Set.class.equals(field.getType())) {
            fieldValue = new HashSet<>(createListAndIterateOverRange(range, annotation));
        } else if (Collection.class.equals(field.getType())) {
            fieldValue = createListAndIterateOverRange(range, annotation);
        }

        return fieldValue;
    }

    private void validate() {
        if (!field.isAnnotationPresent(CellCollection.class)) {
            throw new MissingAnnotationException(
                    String.format("Annotation %s is not present", CellCollection.class.getCanonicalName())
            );
        }
        if (!field.getType().equals(Collection.class)
                && !field.getType().equals(Set.class)
                && !field.getType().equals(List.class)) {
            throw new TypeIsNotSupportedException(String.format(
                    "Could not process the field '%s' which has a type '%s'. " +
                            "You might need to use another annotation", field.getName(), field.getType()));
        }
    }

    private Collection<Object> createListAndIterateOverRange(CellRangeAddress range, CellCollection annotation) {
        Collection<Object> collection = new ArrayList<>();
        Class<?> clazz = (Class<?>) getClassFromGenericField(field)[0];
        int counter = 0;
        for (CellAddress cellAddress : range) {
            org.apache.poi.ss.usermodel.Cell cell = getOrCreateCell(sheet, cellAddress);
            Object cellValue;
            if (!ifTypeIsPureObject(clazz)) {
                cellValue = read(
                        sheet, clazz, new CellIndexTracker(counter, annotation.strategy(), range));
            } else {
                if (StringUtils.isNullOrEmpty(readStraightTypeFromExcel(cell))) {
                    break;
                }
                cellValue = readSingleValueFromSheet(clazz, cell);
            }
            collection.add(cellValue);
            counter += annotation.step();
        }
        return collection;
    }

    private Collection<Object> createListAndIterateUtilEmpty(CellRangeAddress range, CellCollection annotation) {
        if (!List.class.equals(field.getType())
                && !Set.class.equals(field.getType())
                && !Collection.class.equals(field.getType())) {
            throw new IllegalStateException("Incorrect field type. Collection is required");
        }

        int index = CellStrategy.COLUMN_UNTIL_NULL.equals(annotation.strategy())
                ? range.getFirstColumn()
                : range.getFirstRow();

        List<Object> list = new ArrayList<>();
        Class<?> clazz = (Class<?>) getClassFromGenericField(field)[0];

        while (true) {
            org.apache.poi.ss.usermodel.Cell cell = CellStrategy.COLUMN_UNTIL_NULL.equals(annotation.strategy())
                    ? ExcelUtils.createOrGetCell(sheet, range.getFirstRow(), index)
                    : ExcelUtils.createOrGetCell(sheet, index, range.getFirstColumn());

            if (StringUtils.isNullOrEmpty(readStraightTypeFromExcel(cell))) {
                break;
            }

            Object cellValue = readSingleValueFromSheet(clazz, cell);
            list.add(cellValue);
            index += annotation.step();
        }

        if (Set.class.equals(field.getType())) {
            return new HashSet<>(list);
        } else if (List.class.equals(field.getType()) || Collection.class.equals(field.getType())) {
            return list;
        }

        throw new HasNotImplementedYetException(String.format("Have no implementation for %s." +
                " Please consider these collection to be replaced by: Collection, List, Set", field.getType().getCanonicalName()));
    }
}
