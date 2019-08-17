package com.sombrainc.excelorm.e2.impl;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

import static com.sombrainc.excelorm.utils.ExcelUtils.readGenericValueFromSheet;

public class BindField {
    private final Cell cell;
    private final FormulaEvaluator formulaEvaluator;

    public BindField(Cell cell, FormulaEvaluator formulaEvaluator) {
        this.cell = cell;
        this.formulaEvaluator = formulaEvaluator;
    }

    public <T1> T1 toType(Class<T1> type) {
        return parse(type);
    }

    public int toInt() {
        return parse(int.class);
    }

    public long toLong() {
        return parse(long.class);
    }

    public String toText() {
        return parse(String.class);
    }

    public double toDouble() {
        return parse(double.class);
    }

    public Cell cell() {
        return cell;
    }

    private <T1> T1 parse(Class<T1> type) {
        return readGenericValueFromSheet(type, cell, formulaEvaluator);
    }
}
