package com.sombrainc.excelorm.e2.impl.list;

import com.sombrainc.excelorm.e2.impl.CoreExecutor;
import com.sombrainc.excelorm.exception.TypeIsNotSupportedException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.sombrainc.excelorm.utils.ExcelUtils.*;
import static com.sombrainc.excelorm.utils.TypesUtils.isPureObject;

public class ListOfRangeExecutor<T> extends CoreExecutor<List<T>> {
    private final ListOfRange<T> target;

    protected ListOfRangeExecutor(ListOfRange<T> target) {
        super(target.getEReaderContext());
        this.target = target;
    }

    @Override
    public List<T> go() {
        if (!isPureObject(target.aClass)) {
            throw new TypeIsNotSupportedException("Object is not supported. Please see the list of supported objects for this method.");
        }
        final CellRangeAddress addresses = obtainRange(target.range);
        final List<T> list = new ArrayList<>();
        FormulaEvaluator formulaEvaluator = target.getEReaderContext().getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
        for (CellAddress address : addresses) {
            final Cell cell = getOrCreateCell(target.getEReaderContext().getSheet(), address);
            if (Optional.ofNullable(target.until).map(func -> func.apply(cell)).orElse(false)) {
                break;
            }
            if (!Optional.ofNullable(target.filter).map(func -> func.apply(cell)).orElse(true)) {
                continue;
            }
            final T item = readRequestedType(formulaEvaluator, cell, target.mapper, target.aClass);
            list.add(item);
        }
        return list;
    }
}
