package com.sombrainc.excelorm.e2.impl;

import com.sombrainc.excelorm.exception.IncorrectRangeException;
import com.sombrainc.excelorm.exception.TypeIsNotSupportedException;
import com.sombrainc.excelorm.utils.ReflectionUtils;
import lombok.Getter;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

import static com.sombrainc.excelorm.utils.ExcelUtils.getOrCreateCell;
import static com.sombrainc.excelorm.utils.ExcelUtils.readGenericValueFromSheet;
import static com.sombrainc.excelorm.utils.ReflectionUtils.getInstance;
import static com.sombrainc.excelorm.utils.TypesUtils.isPureObject;

@Getter
public abstract class CoreExecutor<T> {
    protected final EReaderContext context;

    protected CoreExecutor(EReaderContext context) {
        this.context = context;
    }

    public abstract T go();

    protected<R> R readForSingleObject(List<Pair<Bind, CellRangeAddress>> pairs, Class<R> aClass, FormulaEvaluator formulaEvaluator) {
        final R instance = getInstance(aClass);
        final Field[] allFields = FieldUtils.getAllFields(aClass);
        for (Field field : allFields) {
            final Pair<Bind, CellRangeAddress> bindField = pairs.stream()
                    .filter(pair -> pair.getKey().getField().equals(field.getName())).findFirst().orElse(null);
            if (bindField == null) {
                continue;
            }
            if (isCollection(field.getType())) {
                readSingleFieldAsCollection(formulaEvaluator, instance, field, bindField);
                continue;
            }
            if (!isPureObject(field.getType())) {
                continue;
            }
            final Object fieldValue = parseValueFromSheet(formulaEvaluator, field.getType(), bindField, toCell(bindField.getValue().iterator().next()));
            ReflectionUtils.setFieldViaReflection(instance, field, fieldValue);
        }
        return instance;
    }

    private<R> void readSingleFieldAsCollection(FormulaEvaluator formulaEvaluator, R instance, Field field, Pair<Bind, CellRangeAddress> bindField) {
        final Collection<Object> collection = new ArrayList<>();
        for (CellAddress address : bindField.getRight()) {
            final Cell cell = toCell(address);
            if (!Optional.ofNullable(bindField.getKey().getUntil()).map(func -> func.apply(cell)).orElse(true)) {
                break;
            }
            if (Optional.ofNullable(bindField.getKey().getFilter()).map(func -> func.apply(cell)).orElse(false)) {
                continue;
            }
            final Class<?> type = (Class<?>) ReflectionUtils.getClassFromGenericField(field)[0];
            final Object fieldValue = parseValueFromSheet(formulaEvaluator, type, bindField, cell);
            collection.add(fieldValue);
        }
        if (field.getType().equals(Set.class)) {
            ReflectionUtils.setFieldViaReflection(instance, field, new HashSet<>(collection));
        } else {
            ReflectionUtils.setFieldViaReflection(instance, field, collection);
        }
    }

    private Object parseValueFromSheet(FormulaEvaluator formulaEvaluator, Class<?> aClass, Pair<Bind, CellRangeAddress> bindField, Cell cell) {
        return Optional.ofNullable(bindField.getKey().getMapper()).map(func -> func.apply(cell))
                .orElseGet(() -> readGenericValueFromSheet(aClass, cell, formulaEvaluator));
    }

    protected static boolean isCollection(Class<?> aClass) {
        return aClass.equals(Collection.class)
                || aClass.equals(Set.class)
                || aClass.equals(List.class);
    }

    protected Sheet getSheet() {
        return context.getSheet();
    }

    protected Cell toCell(CellAddress address) {
        return getOrCreateCell(getSheet(), address);
    }

    protected static boolean isVector(CellRangeAddress addresses) {
        return addresses.getFirstRow() == addresses.getLastRow()
                || addresses.getFirstColumn() == addresses.getLastColumn();
    }

    protected static boolean isHorizontal(CellRangeAddress addresses) {
        return addresses.getFirstRow() == addresses.getLastRow();
    }

    protected static boolean isVertical(CellRangeAddress addresses) {
        return addresses.getFirstColumn() == addresses.getLastColumn();
    }

    protected CellRangeAddress adjustRangeBasedOnVector(CellRangeAddress range, int counter, CellRangeAddress lookForRange) {
        if (isVertical(lookForRange)) {
            return new CellRangeAddress(range.getFirstRow() + counter, range.getLastRow() + counter,
                    range.getFirstColumn(), range.getLastColumn());
        } else if (isVertical(range)) {
            return new CellRangeAddress(range.getFirstRow(), range.getLastRow(),
                    range.getFirstColumn() + counter, range.getLastColumn() + counter);
        }
        throw new IncorrectRangeException("For user custom object the range should be on the same column/row");
    }

    protected FormulaEvaluator createFormulaEvaluator() {
        return getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
    }

    protected <R> R readRequestedType(FormulaEvaluator formulaEvaluator, Cell keyCell, Function<Cell, R> keyMapper, Class<R> keyClass) {
        return Optional.ofNullable(keyMapper).map(func -> func.apply(keyCell))
                .orElseGet(() -> readGenericValueFromSheet(keyClass, keyCell, formulaEvaluator));
    }

    protected void validateOnPureObject(Class aClass, String message) {
        if (!isPureObject(aClass)) {
            throw new TypeIsNotSupportedException(message + ". Please see the list of supported objects for this method");
        }
    }

}
