package com.sombrainc.excelorm.model;

import com.sombrainc.excelorm.annotation.CellCollection;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Field;

public class CellCollectionPresenter {
    private CellCollection annotation;

    private CellRangeAddress range;

    public CellCollectionPresenter(Field field) {
        annotation = field.getAnnotation(CellCollection.class);
        range = CellRangeAddress.valueOf(annotation.cells());
    }

    public CellCollection getAnnotation() {
        return annotation;
    }

    public CellRangeAddress getRange() {
        return range;
    }
}
