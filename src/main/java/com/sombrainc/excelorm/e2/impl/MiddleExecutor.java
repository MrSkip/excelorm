package com.sombrainc.excelorm.e2.impl;

import com.sombrainc.excelorm.exception.IncorrectRangeException;
import com.sombrainc.excelorm.exception.POIRuntimeException;
import com.sombrainc.excelorm.exception.TypeIsNotSupportedException;
import com.sombrainc.excelorm.utils.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

import static com.sombrainc.excelorm.utils.ExcelUtils.readGenericValueFromSheet;
import static com.sombrainc.excelorm.utils.ReflectionUtils.getInstance;
import static com.sombrainc.excelorm.utils.TypesUtils.isPureObject;

public abstract class MiddleExecutor<T> extends CoreExecutor<T> {
    protected MiddleExecutor(EReaderContext context) {
        super(context);
    }

    protected<R> R readForSingleObject(List<Pair<Bind, CellRangeAddress>> pairOfFields, Class<R> aClass,
                                       FormulaEvaluator formulaEvaluator) {
        validate(pairOfFields);
        final R instance = getInstance(aClass);
        final Field[] allFields = FieldUtils.getAllFields(aClass);
        for (Field field : allFields) {
            final Pair<Bind, CellRangeAddress> pair = pairOfFields.stream()
                    .filter(p -> p.getKey().getField().equals(field.getName())).findFirst().orElse(null);
            if (pair == null) {
                continue;
            }
            if (isCollection(field.getType())) {
                readSingleFieldAsCollection(formulaEvaluator, instance, field, pair);
                continue;
            }
            if (!isPureObject(field.getType())) {
                throw new TypeIsNotSupportedException(
                        String.format("The field [%s] is not supported. Please see the list of supported objects for this method", pair.getKey().getField()));
            }
            final Object fieldValue = parseValueFromSheet(formulaEvaluator, field.getType(), pair,
                    new BindField(toCell(pair.getValue().iterator().next()), formulaEvaluator));
            ReflectionUtils.setFieldViaReflection(instance, field, fieldValue);
        }
        return instance;
    }

    private void validate(List<Pair<Bind, CellRangeAddress>> list) {
        for (Pair<Bind, CellRangeAddress> pair : list) {
            if (StringUtils.isBlank(pair.getKey().getField())) {
                throw new POIRuntimeException("Field name could not be empty");
            }
        }
    }

    private<R> void readSingleFieldAsCollection(FormulaEvaluator formulaEvaluator, R instance,
                                                Field field, Pair<Bind, CellRangeAddress> pair) {
        final Collection<Object> collection = new ArrayList<>();
        for (CellAddress address : pair.getRight()) {
            final Cell cell = toCell(address);
            final BindField bindField = new BindField(cell, formulaEvaluator);
            if (!Optional.ofNullable(pair.getKey().getUntil()).map(func -> func.apply(bindField)).orElse(true)) {
                break;
            }
            if (!Optional.ofNullable(pair.getKey().getFilter()).map(func -> func.apply(bindField)).orElse(true)) {
                continue;
            }
            final Class<?> type = (Class<?>) ReflectionUtils.getClassFromGenericField(field)[0];
            final Object fieldValue = parseValueFromSheet(formulaEvaluator, type, pair, bindField);
            collection.add(fieldValue);
        }
        if (field.getType().equals(Set.class)) {
            ReflectionUtils.setFieldViaReflection(instance, field, new HashSet<>(collection));
        } else {
            ReflectionUtils.setFieldViaReflection(instance, field, collection);
        }
    }

    private Object parseValueFromSheet(final FormulaEvaluator formulaEvaluator,
                                       final Class<?> aClass, Pair<Bind, CellRangeAddress> pair,
                                       final BindField userField) {
        return Optional.ofNullable(pair.getKey().getMapper()).map(func -> func.apply(userField))
                .orElseGet(() -> readGenericValueFromSheet(aClass, userField.cell(), formulaEvaluator));
    }

    protected static boolean isCollection(Class<?> aClass) {
        return aClass.equals(Collection.class)
                || aClass.equals(Set.class)
                || aClass.equals(List.class);
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

    protected <R> R readRequestedType(FormulaEvaluator formulaEvaluator, BindField bindField,
                                      Function<BindField, R> keyMapper, Class<R> keyClass) {
        return Optional.ofNullable(keyMapper).map(func -> func.apply(bindField))
                .orElseGet(() -> readGenericValueFromSheet(keyClass, bindField.cell(), formulaEvaluator));
    }

    protected void validateOnPureObject(Class aClass, String message) {
        if (!isPureObject(aClass)) {
            throw new TypeIsNotSupportedException(message + ". Please see the list of supported objects for this method");
        }
    }
}
