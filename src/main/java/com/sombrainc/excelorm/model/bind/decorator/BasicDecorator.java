package com.sombrainc.excelorm.model.bind.decorator;

import org.apache.poi.ss.usermodel.Cell;

import java.util.function.Supplier;

public abstract class BasicDecorator<T> {
    private Supplier<T> value;
    private Cell cell;

    public BasicDecorator(Supplier<T> value, Cell cell) {
        this.value = value;
        this.cell = cell;
    }

    /**
     * Auto pre-read value
     *
     * @return requested user's value from the sheet
     */
    public T getValue() {
        return value.get();
    }

    /**
     * Cell from the sheet
     *
     * @return Apache's @Cell class
     */
    public Cell getCell() {
        return cell;
    }
}
