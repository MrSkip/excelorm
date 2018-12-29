package com.sombrainc.excelorm.model;

import com.sombrainc.excelorm.annotation.CellMap;
import com.sombrainc.excelorm.utils.ExcelValidation;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Field;

public class CellMapPresenter {
    private CellMap annotation;

    private CellRangeAddress keyRange;
    private CellRangeAddress valueRange;

    public CellMapPresenter(Field field) {
        annotation = field.getAnnotation(CellMap.class);
        keyRange = ExcelValidation
                .validateRange(annotation.keyCell(), field)
                .validateAndGetRange();
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

    public CellRangeAddress getValueRange() {
        return valueRange;
    }
}
