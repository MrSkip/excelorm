package com.sombrainc.excelorm.implementor;

import com.sombrainc.excelorm.annotation.CellMap;
import com.sombrainc.excelorm.annotation.CellMark;
import com.sombrainc.excelorm.annotation.Cell;
import com.sombrainc.excelorm.implementor.tactic.CellTypeHandler;
import com.sombrainc.excelorm.implementor.tactic.CellTypeMap;
import com.sombrainc.excelorm.implementor.tactic.CellTypeMark;
import com.sombrainc.excelorm.implementor.tactic.CellTypePosition;
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
        E instance = getInstance(target);

        Field[] allFields = FieldUtils.getAllFields(target);
        for (Field field : allFields) {
            CellTypeHandler handler = null;

            if (field.isAnnotationPresent(Cell.class)) {
                handler = new CellTypePosition<>(field, instance, sheet, tracker);
            } else if (field.isAnnotationPresent(CellMap.class)) {
                handler = new CellTypeMap<>(field, instance, sheet, tracker);
            } else if (field.isAnnotationPresent(CellMark.class)) {
                handler = new CellTypeMark<>(field, instance, sheet, tracker);
            }

            if (handler != null) {
                setFieldViaReflection(instance, field, handler.process());
            }
        }

        return instance;
    }

}
