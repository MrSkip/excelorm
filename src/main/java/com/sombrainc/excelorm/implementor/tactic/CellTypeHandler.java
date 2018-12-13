package com.sombrainc.excelorm.implementor.tactic;

import org.apache.poi.ss.usermodel.Sheet;

import java.lang.reflect.Field;

public interface CellTypeHandler {

    /**
     *
     * @param field
     * @param instance
     * @param sheet
     * @param <E> instance of the target class
     * @return
     */
    <E> Object process(Field field, E instance, Sheet sheet);

}
