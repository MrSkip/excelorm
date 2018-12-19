package com.sombrainc.excelorm.model;

import com.sombrainc.excelorm.annotation.Cell;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Field;

public class CellPositionPresenter {
    private Cell annotation;
    private CellRangeAddress range;

    public CellPositionPresenter(Field field) {
        annotation = field.getAnnotation(Cell.class);
        range = CellRangeAddress.valueOf(annotation.position());
    }

    public Cell getAnnotation() {
        return annotation;
    }

    public CellRangeAddress getRange() {
        return range;
    }
}
