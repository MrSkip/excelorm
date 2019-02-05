package com.sombrainc.excelorm.model.bind;

import com.sombrainc.excelorm.Main;
import com.sombrainc.excelorm.enumeration.CellType;

import java.util.function.Function;

public class SheetQueryExposure<T extends BasicDecorator, R> implements Main.CellSelect<T, R> {

    @Override
    public Main.CellSelect<T, R> select(String address) {
        return null;
    }

    @Override
    public Main.CellSelect<T, R> as(Function<T, R> mapper) {
        return null;
    }

    @Override
    public Main.CellSelect<T, R> from(CellType type) {
        return null;
    }

    @Override
    public Main.CellSelect<T, R> where(Function<T, Boolean> criteria) {
        return null;
    }

    @Override
    public Main.CellSelect<T, R> until(Function<T, Boolean> criteria) {
        return null;
    }

    @Override
    public void limit(int limit) {

    }
}
