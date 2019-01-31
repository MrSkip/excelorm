package com.sombrainc.excelorm.model;

import com.sombrainc.excelorm.annotation.Cell;
import com.sombrainc.excelorm.utils.ExcelValidation;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Field;

public class CellSinglePresenter {
    private Cell annotation;
    private CellRangeAddress range;

    public CellSinglePresenter(Field field) {
        annotation = field.getAnnotation(Cell.class);
        range = ExcelValidation
                .validateRange(annotation.value(), field).straightRange();
    }

    public Cell getAnnotation() {
        return annotation;
    }

    public CellRangeAddress getRange() {
        return range;
    }
}
