package com.sombrainc.excelorm.implementor.tactic;

import com.sombrainc.excelorm.annotation.Cell;
import com.sombrainc.excelorm.annotation.CellCollection;
import com.sombrainc.excelorm.annotation.CellMap;
import com.sombrainc.excelorm.enumeration.CellStrategy;
import com.sombrainc.excelorm.implementor.CellIndexTracker;
import com.sombrainc.excelorm.model.CellCollectionPresenter;
import com.sombrainc.excelorm.model.CellMapPresenter;
import com.sombrainc.excelorm.model.CellSinglePresenter;
import com.sombrainc.excelorm.utils.ExcelUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

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

    private static CellRangeAddress getCellAddresses(CellStrategy strategy, CellRangeAddress range, int index) {
        CellRangeAddress modifiedRange;
        if (strategy == CellStrategy.ROW_UNTIL_NULL) {
            modifiedRange = new CellRangeAddress(
                    range.getFirstRow() + index, range.getLastRow() + index,
                    range.getFirstColumn(), range.getLastColumn()
            );
        } else if (strategy == CellStrategy.COLUMN_UNTIL_NULL) {
            modifiedRange = new CellRangeAddress(
                    range.getFirstRow(), range.getLastRow(),
                    range.getFirstColumn() + index, range.getLastColumn() + index
            );
        } else if (ExcelUtils.isOneCellSelected(range)) {
            modifiedRange = new CellRangeAddress(
                    range.getFirstRow() + index, range.getLastRow() + index,
                    range.getFirstColumn(), range.getLastColumn()
            );
        } else {
            modifiedRange = new CellRangeAddress(
                    range.getFirstRow() + index, range.getLastRow() + index,
                    range.getFirstColumn(), range.getLastColumn()
            );
        }
        return modifiedRange;
    }

    protected CellRangeAddress rearrangeCell() {
        if (field.isAnnotationPresent(CellMap.class)) {
            CellMapPresenter presenter = new CellMapPresenter(field);
            CellStrategy strategy = chooseStrategy(presenter.getAnnotation().strategy());
            CellRangeAddress valueRange = presenter.getValueRange();
            return arrangeCell(strategy, valueRange);
        } else if (field.isAnnotationPresent(CellCollection.class)) {
            CellCollectionPresenter presenter = new CellCollectionPresenter(field);
            CellStrategy strategy = chooseStrategy(presenter.getAnnotation().strategy());
            CellRangeAddress range = presenter.getRange();
            return arrangeCell(strategy, range);
        } else if (field.isAnnotationPresent(Cell.class)) {
            CellSinglePresenter presenter = new CellSinglePresenter(field);
            CellStrategy strategy = chooseStrategy(CellStrategy.FIXED);
            CellRangeAddress range = presenter.getRange();
            return arrangeCell(strategy, range);
        }
        throw new NullPointerException("Implementation not found");
    }

    protected CellRangeAddress arrangeCell(CellStrategy strategy, CellRangeAddress range) {
        int index = tracker.getListItemCounter();
        return getCellAddresses(strategy, range, index);
    }

    private CellStrategy chooseStrategy(CellStrategy fieldStrategy) {
        return tracker.getStrategy() == null
                ? fieldStrategy
                : tracker.getStrategy();
    }

}
