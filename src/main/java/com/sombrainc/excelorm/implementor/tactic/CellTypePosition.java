package com.sombrainc.excelorm.implementor.tactic;

import org.apache.poi.ss.usermodel.Sheet;

import java.lang.reflect.Field;

public class CellTypePosition implements CellTypeHandler {

    @Override
    public <E> Object process(Field field, E instance, Sheet sheet) {
        return null;
    }

}
