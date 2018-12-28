package com.sombrainc.excelorm.model;

import com.sombrainc.excelorm.annotation.CellMap;
import com.sombrainc.excelorm.utils.ExcelUtils;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Field;

public class CellMapPresenter {
    private CellMap annotation;

    private CellRangeAddress keyRange;
    private CellRangeAddress valueRange;

    public CellMapPresenter(Field field) {
        annotation = field.getAnnotation(CellMap.class);
        keyRange = ExcelUtils.validateRange(annotation.keyCell(), "");
        if (!annotation.valueCell().isEmpty()) {
            valueRange = CellRangeAddress.valueOf(annotation.valueCell());
        }
    }

    public CellMap getAnnotation() {
        return annotation;
    }

    public CellRangeAddress getKeyRange() {
        return keyRange;
    }

    public void setKeyRange(CellRangeAddress keyRange) {
        this.keyRange = keyRange;
    }

    public CellRangeAddress getValueRange() {
        return valueRange;
    }

    public void setValueRange(CellRangeAddress valueRange) {
        this.valueRange = valueRange;
    }
}
