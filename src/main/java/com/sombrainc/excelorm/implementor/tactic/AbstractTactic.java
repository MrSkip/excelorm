package com.sombrainc.excelorm.implementor.tactic;

import com.sombrainc.excelorm.implementor.CellIndexTracker;
import org.apache.poi.ss.usermodel.Sheet;

import java.lang.reflect.Field;

public abstract class AbstractTactic<E> {
    protected Field field;
    protected E instance;
    protected Sheet sheet;
    protected CellIndexTracker tracker;

    public AbstractTactic(Field field, E instance, Sheet sheet, CellIndexTracker tracker) {
        this.field = field;
        this.instance = instance;
        this.sheet = sheet;
        this.tracker = tracker;
    }
}
