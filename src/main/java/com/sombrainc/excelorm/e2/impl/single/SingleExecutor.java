package com.sombrainc.excelorm.e2.impl.single;

import com.sombrainc.excelorm.Excelorm;
import com.sombrainc.excelorm.e2.impl.Bind;
import com.sombrainc.excelorm.e2.impl.CoreExecutor;
import com.sombrainc.excelorm.utils.ExcelUtils;
import lombok.Getter;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static com.sombrainc.excelorm.utils.ExcelUtils.*;
import static com.sombrainc.excelorm.utils.ExcelValidation.isOneCellSelected;
import static com.sombrainc.excelorm.utils.ReflectionUtils.getInstance;
import static com.sombrainc.excelorm.utils.TypesUtils.isPureObject;

@Getter
public class SingleExecutor<T> extends CoreExecutor<T> {
    private final SinglePick<T> target;

    public SingleExecutor(SinglePick<T> target) {
        super(target.getEReaderContext());
        this.target = target;
    }

    @Override
    public T go() {
        if (!isPureObject(target.aClass)) {
            if (target.binds.isEmpty()) {
                return Excelorm.read(getSheet(), target.aClass);
            }
            List<Pair<Bind, CellRangeAddress>> bindOfPairs = target.binds.stream()
                    .map(bind -> Pair.of(bind, obtainRange(bind.getCell()))).collect(Collectors.toList());
            return readForSingleObject(bindOfPairs, target.aClass, createFormulaEvaluator());
        }
        CellRangeAddress cellAddresses = obtainRange(target.cell);
        if (!isOneCellSelected(cellAddresses)) {
            System.out.println("Non-single cell found. Only the first cell will be processed");
        }
        Cell cell = getOrCreateCell(target.getEReaderContext().getSheet(), cellAddresses.getFirstRow(), cellAddresses.getFirstColumn());
        if (target.mapper == null) {
            return ExcelUtils.readGenericValueFromSheet(target.aClass, cell, createFormulaEvaluator());
        }
        return target.mapper.apply(cell);
    }

    private T readForSingleObject(List<Pair<Bind, CellRangeAddress>> pairs, Class<T> aClass, FormulaEvaluator formulaEvaluator) {
        final T instance = getInstance(aClass);
        final Field[] allFields = FieldUtils.getAllFields(aClass);
        for (Field field : allFields) {
            final Pair<Bind, CellRangeAddress> bindField = pairs.stream()
                    .filter(pair -> pair.getKey().getField().equals(field.getName())).findFirst().orElse(null);
            if (bindField == null) {
                continue;
            }
            if (isSupportedCollection(field)) {
                for (CellAddress address : bindField.getRight()) {
                    final Cell cell = toCell(address);
                    if (!Optional.ofNullable(bindField.getKey().getUntil()).map(func -> func.apply(cell)).orElse(true)) {
                        break;
                    }
                    if (Optional.ofNullable(bindField.getKey().getFilter()).map(func -> func.apply(cell)).orElse(false)) {
                        continue;
                    }
//                    readRequestedType(formulaEvaluator, cell, bindField.getKey().getMapper(), field.getDeclaringClass())
                }
            }
            if (bindField.getKey().getMapper() == null) {
                readSingleValueFromSheet(field.getType(), getOrCreateCell(getSheet(), bindField.getRight().iterator().next()));
            }
        }
        return instance;
    }

    public static boolean isSupportedCollection(Field field) {
        return field.getType().equals(Collection.class)
                || field.getType().equals(Set.class)
                || field.getType().equals(List.class);
    }
}
