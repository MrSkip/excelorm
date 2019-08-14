package com.sombrainc.excelorm.e2.impl.single;

import com.sombrainc.excelorm.e2.impl.CoreExecutor;
import com.sombrainc.excelorm.utils.ExcelUtils;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;

import static com.sombrainc.excelorm.utils.ExcelUtils.getOrCreateCell;
import static com.sombrainc.excelorm.utils.ExcelUtils.obtainRange;
import static com.sombrainc.excelorm.utils.ExcelValidation.isOneCellSelected;

@Getter
public class SingleExecutor<T> extends CoreExecutor<T> {
    private final SinglePick<T> target;

    public SingleExecutor(SinglePick<T> target) {
        super(target.getEReaderContext());
        this.target = target;
    }

    @Override
    public T go() {
        validateOnPureObject(target.aClass, "Object is not supported");
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
