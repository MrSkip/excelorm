package com.sombrainc.excelorm.e2.impl.list;

import com.sombrainc.excelorm.e2.impl.Bind;
import com.sombrainc.excelorm.e2.impl.CoreActions;
import com.sombrainc.excelorm.e2.impl.CoreExecutor;
import com.sombrainc.excelorm.e2.impl.EReaderContext;
import com.sombrainc.excelorm.e2.impl.single.SinglePick;
import org.apache.poi.ss.usermodel.Cell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class ListOfRange<T> extends CoreActions<List<T>> {
    protected Function<Cell, T> mapper;
    protected Function<Cell, Boolean> filter;
    protected Function<Cell, Boolean> until;
    protected String range;
    protected Class<T> aClass;
    protected List<Bind> binds;

    public ListOfRange(EReaderContext EReaderContext, Class<T> aClass) {
        super(EReaderContext);
        this.aClass = aClass;
        this.binds = new ArrayList<>();
    }

    public ListOfRange(EReaderContext EReaderContext, ListOfRange<T> self) {
        super(EReaderContext);
        this.aClass = self.aClass;
        this.range = self.range;
        this.mapper = self.mapper;
        this.filter = self.filter;
        this.until = self.until;
        this.binds = self.binds;
    }

    @Override
    protected CoreExecutor<List<T>> invokeExecutor() {
        return new ListOfRangeExecutor<>(this);
    }

    public ListOfRange<T> binds(Bind... binds) {
        this.binds = Arrays.asList(Objects.requireNonNull(binds));
        return new ListOfRange<>(getEReaderContext(), this);
    }

    public ListOfRange<T> pick(String range) {
        this.range = range;
        return new ListOfRange<>(getEReaderContext(), this);
    }

    public ListOfRange<T> map(Function<Cell, T> mapper) {
        this.mapper = mapper;
        return new ListOfRange<>(this.getEReaderContext(), this);
    }

    public ListOfRange<T> filter(Function<Cell, Boolean> filter) {
        this.filter = filter;
        return new ListOfRange<>(this.getEReaderContext(), this);
    }

    public ListOfRange<T> until(Function<Cell, Boolean> until) {
        this.until = until;
        return new ListOfRange<>(this.getEReaderContext(), this);
    }

}
