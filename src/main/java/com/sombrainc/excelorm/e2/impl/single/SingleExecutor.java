package com.sombrainc.excelorm.e2.impl.single;

import com.sombrainc.excelorm.Excelorm;
import com.sombrainc.excelorm.e2.impl.Bind;
import com.sombrainc.excelorm.e2.impl.CoreExecutor;
import com.sombrainc.excelorm.utils.ExcelUtils;
import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.*;
import java.util.stream.Collectors;

import static com.sombrainc.excelorm.utils.ExcelUtils.*;
import static com.sombrainc.excelorm.utils.ExcelValidation.isOneCellSelected;
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
                // assume that annotations are on the object
                return Excelorm.read(getSheet(), target.aClass);
            }
            // custom user definition of the object
            List<Pair<Bind, CellRangeAddress>> bindOfPairs = target.binds.stream()
                    .map(bind -> Pair.of(bind, obtainRange(bind.getInitialCell()))).collect(Collectors.toList());
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

}
