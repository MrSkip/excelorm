package com.sombrainc.excelorm.implementor;

import com.sombrainc.excelorm.enumeration.CellStrategy;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * To keep track of index/strategy while iterating over collection
 */
public class CellIndexTracker {
    // index of next row/column to be loaded into collection
    private int listItemCounter;
    // parent strategy
    private CellStrategy strategy;
    // parent key range
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

    public CellStrategy getStrategy() {
        return strategy;
    }

    public CellRangeAddress getParentRange() {
        return parentRange;
    }
}
