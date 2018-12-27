package com.sombrainc.excelorm.implementor;

import com.sombrainc.excelorm.enumeration.CellStrategy;
import org.apache.poi.ss.util.CellRangeAddress;

public class CellIndexTracker {
    private int listItemCounter;
    private CellStrategy strategy;
    private CellRangeAddress parentRange;

    public CellIndexTracker() {
        this.listItemCounter = 0;
    }

    public CellIndexTracker(int listItemCounter, CellStrategy strategy, CellRangeAddress parentRange) {
        this.listItemCounter = listItemCounter;
        this.strategy = strategy;
        this.parentRange = parentRange;
    }

    public int getListItemCounter() {
        return listItemCounter;
    }

    public void setListItemCounter(int listItemCounter) {
        this.listItemCounter = listItemCounter;
    }

    public CellStrategy getStrategy() {
        return strategy;
    }

    public CellRangeAddress getParentRange() {
        return parentRange;
    }
}
