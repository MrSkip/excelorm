package com.sombrainc.excelorm.e2.utils;

import com.sombrainc.excelorm.e2.impl.BindField;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

import java.util.function.Function;

import static com.sombrainc.excelorm.constants.CommonConstants.DATA_FORMATTER;

public class EFilters {

    public static boolean isBlank(Cell cell) {
        if (cell == null) {
            return true;
        }
        final FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
        return DATA_FORMATTER.formatCellValue(cell, evaluator).trim().isEmpty();
    }

    public static Function<BindField, Boolean> isBlank() {
        return field -> isBlank(field.poi());
    }

    public static boolean isNotBlank(Cell cell) {
        if (cell == null) {
            return false;
        }
        final FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
        return !DATA_FORMATTER.formatCellValue(cell, evaluator).trim().isEmpty();
    }

    public static boolean isNotBlank(BindField field) {
        return isNotBlank(field.poi());
    }

    public static Function<BindField, Boolean> isNotBlank() {
        return field -> isNotBlank(field.poi());
    }

    public static Function<BindField, Boolean> contains(String str) {
        return cell -> cell.toText().contains(str);
    }
}
