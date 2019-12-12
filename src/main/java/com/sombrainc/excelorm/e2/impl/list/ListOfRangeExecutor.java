package com.sombrainc.excelorm.e2.impl.list;

import com.sombrainc.excelorm.e2.impl.Bind;
import com.sombrainc.excelorm.e2.impl.BindField;
import com.sombrainc.excelorm.e2.impl.MiddleExecutor;
import com.sombrainc.excelorm.exception.IncorrectRangeException;
import com.sombrainc.excelorm.exception.POIRuntimeException;
import com.sombrainc.excelorm.exception.TypeIsNotSupportedException;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.ArrayList;
import java.util.List;

import static com.sombrainc.excelorm.e2.utils.FunctionUtils.filterFunction;
import static com.sombrainc.excelorm.e2.utils.FunctionUtils.untilFunction;
import static com.sombrainc.excelorm.utils.ExcelUtils.obtainRange;
import static com.sombrainc.excelorm.utils.TypesUtils.isAmongDefinedTypes;

public class ListOfRangeExecutor<T> extends MiddleExecutor<List<T>> {
    private final ListOfRange<T> target;

    ListOfRangeExecutor(ListOfRange<T> target) {
        super(target.getEReaderContext());
        this.target = target;
    }

    @Override
    public List<T> execute() {
        validateIncomingParams();
        final List<Pair<Bind, CellRangeAddress>> bindOfPairs = getBinds(target.binds);
        final CellRangeAddress addresses = obtainRange(target.range);
        final List<T> list = new ArrayList<>();
        final FormulaEvaluator evaluator = createFormulaEvaluator();
        int simpleCounter = -1;
        for (CellAddress address : addresses) {
            simpleCounter++;
            final Cell cell = toCell(address);
            final BindField bindField = new BindField(cell, evaluator);
            if (untilFunction(target.until, bindField)) {
                break;
            }
            if (filterFunction(target.filter, bindField)) {
                continue;
            }
            T item = readAsPureOrCustomObject(bindOfPairs, addresses,
                    evaluator, simpleCounter, bindField, target.mapper, target.aClass);
            list.add(item);
        }
        return list;
    }

    private void validateIncomingParams() {
        if (target.aClass == null) {
            throw new POIRuntimeException("Generic class is null");
        }
        if (!isAmongDefinedTypes(target.aClass)) {
            if (target.binds.isEmpty() && target.mapper == null) {
                throw new TypeIsNotSupportedException("Check if the mapped object is supported or define the object fields to be bind");
            }
            if (!isVector(obtainRange(target.range))) {
                throw new IncorrectRangeException("For user custom object the range should be on the same column/row");
            }
        }
    }
}
