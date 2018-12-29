package com.sombrainc.excelorm.implementor.tactic.implementation;

import com.sombrainc.excelorm.Excelorm;
import com.sombrainc.excelorm.annotation.CellMark;
import com.sombrainc.excelorm.exception.MissingAnnotationException;
import com.sombrainc.excelorm.implementor.CellIndexTracker;
import com.sombrainc.excelorm.implementor.tactic.AbstractTactic;
import com.sombrainc.excelorm.implementor.tactic.CellTypeHandler;
import org.apache.poi.ss.usermodel.Sheet;

import java.lang.reflect.Field;

public class CellTypeMark<E> extends AbstractTactic<E> implements CellTypeHandler {

    public CellTypeMark(Field field, E instance, Sheet sheet, CellIndexTracker tracker) {
        super(field, instance, sheet, tracker);
    }

    @Override
    public Object process() {
        if (!field.isAnnotationPresent(CellMark.class)) {
            throw new MissingAnnotationException(
                    String.format("Annotation %s is not present", CellMark.class.getCanonicalName())
            );
        }

        return Excelorm.read(sheet, field.getType(), tracker);
    }
}
