package com.sombrainc.excelorm.implementor;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.poi.ss.usermodel.Sheet;

import java.lang.reflect.Field;

import static com.sombrainc.excelorm.utils.ReflectionUtils.getInstance;
import static com.sombrainc.excelorm.utils.ReflectionUtils.setFieldViaReflection;

public class ExcelReader {

    public static <E> E read(Sheet sheet, Class<E> target) {
        CellIndexTracker tracker = new CellIndexTracker();
        return read(sheet, target, tracker);
    }

    public static <E> E read(Sheet sheet, Class<E> target, CellIndexTracker tracker) {
        if (sheet == null) {
            throw new RuntimeException("Sheet is null");
        }
        E instance = getInstance(target);
        Field[] allFields = FieldUtils.getAllFields(target);
        for (Field field : allFields) {
            new CellTacticFactory().getCellTactic(field, instance, sheet, tracker)
                    .ifPresent(handler -> setFieldViaReflection(instance, field, handler.process()));
        }
        return instance;
    }

}
