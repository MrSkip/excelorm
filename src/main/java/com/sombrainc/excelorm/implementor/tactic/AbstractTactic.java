package com.sombrainc.excelorm.implementor.tactic;

import com.sombrainc.excelorm.enumeration.CellStrategy;
import com.sombrainc.excelorm.implementor.CellIndexTracker;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Field;

import static com.sombrainc.excelorm.utils.ExcelValidation.isIteratingOverColumns;

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

    private static CellRangeAddress getCellAddresses(CellStrategy strategy, CellRangeAddress cellRange, CellIndexTracker tracker) {
        int index = tracker.getListItemCounter();
        CellRangeAddress modifiedRange;
        if (strategy == CellStrategy.COLUMN_UNTIL_NULL
                || (tracker.getParentRange() != null && isIteratingOverColumns(tracker.getParentRange()))) {
            modifiedRange = new CellRangeAddress(
                    cellRange.getFirstRow(), cellRange.getLastRow(),
                    cellRange.getFirstColumn() + index, cellRange.getLastColumn() + index
            );
        } else {
            modifiedRange = new CellRangeAddress(
                    cellRange.getFirstRow() + index, cellRange.getLastRow() + index,
                    cellRange.getFirstColumn(), cellRange.getLastColumn()
            );
        }
        return modifiedRange;
    }

    protected CellRangeAddress arrangeCell(CellStrategy strategy, CellRangeAddress range) {
        return getCellAddresses(strategy, range, tracker);
    }

    protected CellStrategy chooseStrategy(CellStrategy fieldStrategy) {
        return tracker.getStrategy() == null
                ? fieldStrategy
                : tracker.getStrategy();
    }

}
