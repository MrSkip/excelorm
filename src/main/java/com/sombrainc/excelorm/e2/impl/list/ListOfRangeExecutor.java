package com.sombrainc.excelorm.e2.impl.list;

import com.sombrainc.excelorm.e2.impl.Bind;
import com.sombrainc.excelorm.e2.impl.BindField;
import com.sombrainc.excelorm.e2.impl.CoreExecutor;
import com.sombrainc.excelorm.e2.impl.MiddleExecutor;
import com.sombrainc.excelorm.exception.IncorrectRangeException;
import com.sombrainc.excelorm.exception.TypeIsNotSupportedException;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.sombrainc.excelorm.utils.ExcelUtils.*;
import static com.sombrainc.excelorm.utils.TypesUtils.isPureObject;

public class ListOfRangeExecutor<T> extends MiddleExecutor<List<T>> {
    private final ListOfRange<T> target;

    protected ListOfRangeExecutor(ListOfRange<T> target) {
        super(target.getEReaderContext());
        this.target = target;
    }

    @Override
    public List<T> execute() {
        validate();
        List<Pair<Bind, CellRangeAddress>> bindOfPairs = getBinds();
        final CellRangeAddress addresses = obtainRange(target.range);
        final List<T> list = new ArrayList<>();
        final FormulaEvaluator evaluator = createFormulaEvaluator();
        int simpleCounter = -1;
        for (CellAddress address : addresses) {
            simpleCounter++;
            final Cell cell = toCell(address);
            final BindField bindField = new BindField(cell, evaluator);
            if (Optional.ofNullable(target.until).map(func -> func.apply(bindField)).orElse(false)) {
                break;
            }
            if (!Optional.ofNullable(target.filter).map(func -> func.apply(bindField)).orElse(true)) {
                continue;
            }
            if (isPureObject(target.aClass)) {
                final T item = readRequestedType(evaluator, bindField, target.mapper, target.aClass);
                list.add(item);
            } else {
                int finalSimpleCounter = simpleCounter;
                final List<Pair<Bind, CellRangeAddress>> modifiedPairs = bindOfPairs.stream()
                        .map(pair -> Pair.of(pair.getLeft(), adjustRangeBasedOnVector(pair.getRight(), finalSimpleCounter, addresses))).collect(Collectors.toList());
                final T singleObject = readForSingleObject(modifiedPairs, target.aClass, evaluator);
                list.add(singleObject);
            }
        }
        return list;
    }

    private void validate() {
        if (!isPureObject(target.aClass)) {
            if (target.binds.isEmpty()) {
                throw new TypeIsNotSupportedException("You should explicitly map the object fields");
            }
            if (!isVector(obtainRange(target.range))) {
                throw new IncorrectRangeException("For user custom object the range should be on the same column/row");
            }
        }
    }

    private List<Pair<Bind, CellRangeAddress>> getBinds() {
        if (!target.binds.isEmpty()) {
            return target.binds.stream()
                    .map(bind -> Pair.of(bind, obtainRange(bind.getInitialCell()))).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
