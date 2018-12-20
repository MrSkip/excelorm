package com.sombrainc.excelorm.implementor;

import com.sombrainc.excelorm.enumeration.CellStrategy;

public class CellIndexTracker {
    private int listItemCounter;
    private CellStrategy strategy;

    public CellIndexTracker() {
        this.listItemCounter = 0;
    }

    public CellIndexTracker(int listItemCounter, CellStrategy strategy) {
        this.listItemCounter = listItemCounter;
        this.strategy = strategy;
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
}
