package com.sombrainc.excelorm.e2.impl.single;

import com.sombrainc.excelorm.e2.impl.CoreExecutor;
import com.sombrainc.excelorm.exception.TypeIsNotSupportedException;
import com.sombrainc.excelorm.utils.ExcelUtils;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.util.CellRangeAddress;

import static com.sombrainc.excelorm.utils.ExcelUtils.getOrCreateCell;
import static com.sombrainc.excelorm.utils.ExcelUtils.obtainRange;
import static com.sombrainc.excelorm.utils.ExcelValidation.isOneCellSelected;
import static com.sombrainc.excelorm.utils.TypesUtils.isPureObject;

@Getter
public class SingleExecutor<T> extends CoreExecutor<T> {
    private final SinglePick<T> target;

    public SingleExecutor(SinglePick<T> target) {
        this.target = target;
    }

    @Override
    public T go() {
        if (!isPureObject(target.aClass)) {
            throw new TypeIsNotSupportedException("Object is not supported. Please see the list of supported objects for this method.");
        }
        CellRangeAddress cellAddresses = obtainRange(target.cell);
        if (!isOneCellSelected(cellAddresses)) {
            System.out.println("Non-single cell found. Only the first cell will be processed");
        }
        Cell cell = getOrCreateCell(target.getEReaderContext().getSheet(), cellAddresses.getFirstRow(), cellAddresses.getFirstColumn());
        if (target.mapper == null) {
            FormulaEvaluator formulaEvaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
            return ExcelUtils.readGenericValueFromSheet(target.aClass, cell, formulaEvaluator);
        }
        return target.mapper.apply(cell);
    }
}
