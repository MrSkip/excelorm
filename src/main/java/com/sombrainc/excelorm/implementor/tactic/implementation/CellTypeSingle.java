package com.sombrainc.excelorm.implementor.tactic.implementation;

import com.sombrainc.excelorm.implementor.CellIndexTracker;
import com.sombrainc.excelorm.implementor.tactic.AbstractTactic;
import com.sombrainc.excelorm.implementor.tactic.CellTypeHandler;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Field;
import java.util.Iterator;

import static com.sombrainc.excelorm.utils.ExcelUtils.getOrCreateCell;
import static com.sombrainc.excelorm.utils.ExcelUtils.readSingleValueFromSheet;
import static com.sombrainc.excelorm.utils.TypesUtils.ifTypeIsPureObject;

public class CellTypeSingle<E> extends AbstractTactic<E> implements CellTypeHandler {

    public CellTypeSingle(Field field, E instance, Sheet sheet, CellIndexTracker tracker) {
        super(field, instance, sheet, tracker);
    }

    @Override
    public Object process() {
        CellRangeAddress range = rearrangeCell();

        if (!ifTypeIsPureObject(field.getType())) {
            throw new RuntimeException(String.format("Type is not supported: %s", field.getType()));
        }

        Iterator<CellAddress> iterator = range.iterator();
        return readSingleValueFromSheet(field.getType(), getOrCreateCell(sheet, iterator.next()));
    }

}
