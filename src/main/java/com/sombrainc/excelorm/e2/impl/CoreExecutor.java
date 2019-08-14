package com.sombrainc.excelorm.e2.impl;

import com.sombrainc.excelorm.exception.TypeIsNotSupportedException;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;

import java.util.Optional;
import java.util.function.Function;

import static com.sombrainc.excelorm.utils.ExcelUtils.getOrCreateCell;
import static com.sombrainc.excelorm.utils.ExcelUtils.readGenericValueFromSheet;
import static com.sombrainc.excelorm.utils.TypesUtils.isPureObject;

@Getter
public abstract class CoreExecutor<T> {
    protected final EReaderContext context;

    protected CoreExecutor(EReaderContext context) {
        this.context = context;
    }

    public abstract T go();

    protected Sheet getSheet() {
        return context.getSheet();
    }

    protected Cell toCell(CellAddress address) {
        return getOrCreateCell(getSheet(), address);
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
