package com.sombrainc.excelorm.e2.impl.list;

import com.sombrainc.excelorm.e2.impl.CoreActions;
import com.sombrainc.excelorm.e2.impl.CoreExecutor;
import com.sombrainc.excelorm.e2.impl.EReaderContext;
import org.apache.poi.ss.usermodel.Cell;

import java.util.List;
import java.util.function.Function;

public class ListOfRange<T> extends CoreActions<List<T>> {
    protected Function<Cell, T> mapper;
    protected Function<Cell, Boolean> filter;
    protected Function<Cell, Boolean> until;
    protected String range;
    protected Class<T> aClass;

    public ListOfRange(EReaderContext EReaderContext, Class<T> aClass) {
        super(EReaderContext);
        this.aClass = aClass;
    }

    public ListOfRange(EReaderContext EReaderContext, Class<T> aClass, String range,
                       Function<Cell, T> mapper, Function<Cell, Boolean> filter, Function<Cell, Boolean> until) {
        super(EReaderContext);
        this.aClass = aClass;
        this.range = range;
        this.mapper = mapper;
        this.filter = filter;
        this.until = until;
    }

    @Override
    protected CoreExecutor<List<T>> invokeExecutor() {
        return new ListOfRangeExecutor<>(this);
    }

    public ListOfRange<T> pick(String range) {
        return new ListOfRange<>(getEReaderContext(), this.aClass, range, mapper, filter, until);
    }

    public ListOfRange<T> map(Function<Cell, T> mapper) {
        return new ListOfRange<>(this.getEReaderContext(), this.aClass, range, mapper, filter, until);
    }

    public ListOfRange<T> filter(Function<Cell, Boolean> filter) {
        return new ListOfRange<>(this.getEReaderContext(), this.aClass, range, mapper, filter, until);
    }

    public ListOfRange<T> until(Function<Cell, Boolean> until) {
        return new ListOfRange<>(this.getEReaderContext(), this.aClass, range, mapper, filter, until);
    }

}
