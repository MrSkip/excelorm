package com.sombrainc.excelorm.model;

import com.sombrainc.excelorm.annotation.CellCollection;
import com.sombrainc.excelorm.utils.ExcelUtils;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Field;

public class CellCollectionPresenter {
    private CellCollection annotation;

    private CellRangeAddress range;

    public CellCollectionPresenter(Field field) {
        annotation = field.getAnnotation(CellCollection.class);
        range = ExcelUtils.obtainRange(annotation.cells(), field)
                .straightRange();
    }

    public CellCollection getAnnotation() {
        return annotation;
    }

    public CellRangeAddress getRange() {
        return range;
    }
}
