package com.sombrainc.excelorm.implementor.tactic.implementation;

import com.sombrainc.excelorm.annotation.Cell;
import com.sombrainc.excelorm.enumeration.CellStrategy;
import com.sombrainc.excelorm.exception.MissingAnnotationException;
import com.sombrainc.excelorm.exception.TypeIsNotSupportedException;
import com.sombrainc.excelorm.implementor.CellIndexTracker;
import com.sombrainc.excelorm.implementor.tactic.AbstractTactic;
import com.sombrainc.excelorm.implementor.tactic.CellTypeHandler;
import com.sombrainc.excelorm.model.CellSinglePresenter;
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
        validate();
        CellRangeAddress range = rearrangeCell();
        Iterator<CellAddress> iterator = range.iterator();
        return readSingleValueFromSheet(field.getType(), getOrCreateCell(sheet, iterator.next()));
    }

    private CellRangeAddress rearrangeCell() {
        CellSinglePresenter presenter = new CellSinglePresenter(field);
        CellStrategy strategy = chooseStrategy(CellStrategy.FIXED);
        CellRangeAddress range = presenter.getRange();
        return arrangeCell(strategy, range);
    }

    private void validate() {
        if (!field.isAnnotationPresent(Cell.class)) {
            throw new MissingAnnotationException(
                    String.format("Annotation %s is not present", Cell.class.getCanonicalName())
            );
        }
        if (!ifTypeIsPureObject(field.getType())) {
            throw new TypeIsNotSupportedException(String.format(
                    "Could not process the field '%s' which has a type '%s'. " +
                            "You might need to use another annotation", field.getName(), field.getType()));
        }
    }

}
