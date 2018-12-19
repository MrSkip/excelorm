package com.sombrainc.excelorm.implementor;

import com.sombrainc.excelorm.enumeration.DataQualifier;

public class CellIndexTracker {
    private int listItemCounter;
    private DataQualifier strategy;

    public CellIndexTracker() {
        this.listItemCounter = 0;
    }

    public CellIndexTracker(int listItemCounter, DataQualifier strategy) {
        this.listItemCounter = listItemCounter;
        this.strategy = strategy;
    }

    public int getListItemCounter() {
        return listItemCounter;
    }

    public void setListItemCounter(int listItemCounter) {
        this.listItemCounter = listItemCounter;
    }

    public DataQualifier getStrategy() {
        return strategy;
    }
}
