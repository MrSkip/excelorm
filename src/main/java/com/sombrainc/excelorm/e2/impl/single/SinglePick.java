package com.sombrainc.excelorm.e2.impl.single;

import com.sombrainc.excelorm.e2.impl.CoreActions;
import com.sombrainc.excelorm.e2.impl.CoreExecutor;
import com.sombrainc.excelorm.e2.impl.EReaderContext;
import org.apache.poi.ss.usermodel.Cell;

import java.util.function.Function;

public class SinglePick<T> extends CoreActions<T> {
    protected Function<Cell, T> mapper;
    protected String cell;
    protected Class<T> aClass;

    public SinglePick(EReaderContext EReaderContext, Class<T> aClass) {
        super(EReaderContext);
        this.aClass = aClass;
    }

    public SinglePick(EReaderContext EReaderContext, Class<T> aClass, String cell, Function<Cell, T> mapper) {
        super(EReaderContext);
        this.aClass = aClass;
        this.cell = cell;
        this.mapper = mapper;
    }

    @Override
    protected CoreExecutor<T> invokeExecutor() {
        return new SingleExecutor<>(this);
    }

    public SinglePick<T> pick(String cell) {
        return new SinglePick<>(this.getEReaderContext(), aClass, cell, mapper);
    }

    public SinglePick<T> map(Function<Cell, T> mapper) {
        return new SinglePick<>(this.getEReaderContext(), aClass, cell, mapper);
    }

}
