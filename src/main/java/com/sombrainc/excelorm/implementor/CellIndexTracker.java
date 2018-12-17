package com.sombrainc.excelorm.implementor;

import com.sombrainc.excelorm.annotation.CellMap;
import com.sombrainc.excelorm.annotation.CellPosition;
import com.sombrainc.excelorm.enumeration.DataQualifier;
import com.sombrainc.excelorm.utils.ExcelUtils;
import javafx.util.Pair;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Field;

public class CellIndexTracker {
    private int counter;

    public CellIndexTracker() {
        this.counter = 0;
    }

    public CellIndexTracker(int counter) {
        this.counter = counter;
    }

    public CellIndexTracker incrementCounter() {
        this.counter++;
        return this;
    }

    public CellRangeAddress rearrangeCell(Field field) {
        if (field.isAnnotationPresent(CellMap.class)) {
            CellMapLogic cellLogic = new CellMapLogic(field);
            DataQualifier strategy = cellLogic.getAnnotation().strategy();
            return modifyRangeBasedOnStrategy(cellLogic, strategy);
        } else if (field.isAnnotationPresent(CellPosition.class)) {
            CellPositionLogic cellLogic = new CellPositionLogic(field);
            DataQualifier strategy = cellLogic.getAnnotation().strategy();
            return modifyRangeBasedOnStrategy(cellLogic, strategy);
        }
        return null;
    }

    public Pair<CellRangeAddress, CellRangeAddress> rearrangeMap(Field field) {
        CellMapLogic cellLogic = new CellMapLogic(field);
        DataQualifier strategy = cellLogic.getAnnotation().strategy();

        CellRangeAddress keyRange = arrangeCell(strategy, cellLogic.keyRange);
        CellRangeAddress valueRange = null;

        if (cellLogic.valueRange != null) {
            valueRange = arrangeCell(strategy, cellLogic.valueRange);
        }

        return new Pair<>(keyRange, valueRange);
    }

    private CellRangeAddress modifyRangeBasedOnStrategy(CellMapLogic cellMap, DataQualifier strategy) {
        CellRangeAddress valueRange = cellMap.getValueRange();
        return arrangeCell(strategy, valueRange);
    }

    private CellRangeAddress modifyRangeBasedOnStrategy(CellPositionLogic cellPosition, DataQualifier strategy) {
        CellRangeAddress range = cellPosition.getRange();
        return arrangeCell(strategy, range);
    }

    private CellRangeAddress arrangeCell(DataQualifier strategy, CellRangeAddress range) {
        CellRangeAddress modifiedRange;
        if (strategy == DataQualifier.ROW_UNTIL_NULL) {
            modifiedRange = new CellRangeAddress(
                    range.getFirstRow() + this.counter, range.getLastRow() + this.counter,
                    range.getFirstColumn(), range.getLastColumn()
            );
        } else if (strategy == DataQualifier.COLUMN_UNTIL_NULL) {
            modifiedRange = new CellRangeAddress(
                    range.getFirstRow(), range.getLastRow(),
                    range.getFirstColumn() + this.counter, range.getLastColumn() + this.counter
            );
        } else if (ExcelUtils.isOneCellSelected(range)) {
            modifiedRange = new CellRangeAddress(
                    range.getFirstRow() + this.counter, range.getLastRow() + this.counter,
                    range.getFirstColumn(), range.getLastColumn()
            );
        } else {
            modifiedRange = new CellRangeAddress(
                    range.getFirstRow() + this.counter, range.getLastRow() + this.counter,
                    range.getFirstColumn(), range.getLastColumn()
            );
        }
        // todo add more logic
        return modifiedRange;
    }

    public static class CellMapLogic {
        private CellMap annotation;

        private CellRangeAddress keyRange;
        private CellRangeAddress valueRange;

        private CellMapLogic(Field field) {
            annotation = field.getAnnotation(CellMap.class);
            keyRange = CellRangeAddress.valueOf(annotation.keyCell());
            if (!annotation.firstValueCell().isEmpty()) {
                valueRange = CellRangeAddress.valueOf(annotation.firstValueCell());
            }
        }

        public CellMap getAnnotation() {
            return annotation;
        }

        public CellRangeAddress getKeyRange() {
            return keyRange;
        }

        public void setKeyRange(CellRangeAddress keyRange) {
            this.keyRange = keyRange;
        }

        public CellRangeAddress getValueRange() {
            return valueRange;
        }

        public void setValueRange(CellRangeAddress valueRange) {
            this.valueRange = valueRange;
        }
    }

    public static class CellPositionLogic {
        private CellPosition annotation;
        private CellRangeAddress range;

        public CellPositionLogic(Field field) {
            annotation = field.getAnnotation(CellPosition.class);
            range = CellRangeAddress.valueOf(annotation.position());
        }

        public CellPosition getAnnotation() {
            return annotation;
        }

        public CellRangeAddress getRange() {
            return range;
        }
    }
}
