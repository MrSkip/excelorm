package com.sombrainc.excelorm.e2.impl;

import lombok.Getter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

import java.util.Optional;
import java.util.function.Function;

import static com.sombrainc.excelorm.utils.ExcelUtils.readGenericValueFromSheet;

@Getter
public abstract class CoreExecutor<T> {

    public abstract T go();

    protected <R> R readRequestedType(FormulaEvaluator formulaEvaluator, Cell keyCell, Function<Cell, R> keyMapper, Class<R> keyClass) {
        return Optional.ofNullable(keyMapper).map(func -> func.apply(keyCell))
                .orElseGet(() -> readGenericValueFromSheet(keyClass, keyCell, formulaEvaluator));
    }

}
