package com.sombrainc.excelorm.e2.impl.single;

import com.sombrainc.excelorm.e2.impl.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class SinglePick<T> extends CoreActions<T> {
    protected Function<BindField, T> mapper;
    protected String cell;
    protected Class<T> aClass;
    protected List<Bind> binds;

    public SinglePick(EReaderContext EReaderContext, Class<T> aClass) {
        super(EReaderContext);
        this.aClass = aClass;
        this.binds = new ArrayList<>();
    }

    public SinglePick(EReaderContext EReaderContext, Class<T> aClass,
                      String cell, Function<BindField, T> mapper, List<Bind> binds) {
        super(EReaderContext);
        this.aClass = aClass;
        this.cell = cell;
        this.mapper = mapper;
        this.binds = binds;
    }

    @Override
    protected CoreExecutor<T> invokeExecutor() {
        return new SingleExecutor<>(this);
    }

    public SinglePick<T> pick(String cell) {
        return new SinglePick<>(this.getEReaderContext(), aClass, cell, mapper, binds);
    }

    public SinglePick<T> binds(Bind... binds) {
        this.binds = Arrays.asList(Objects.requireNonNull(binds));
        return new SinglePick<>(this.getEReaderContext(), aClass, cell, mapper, this.binds);
    }

    public SinglePick<T> map(Function<BindField, T> mapper) {
        return new SinglePick<>(this.getEReaderContext(), aClass, cell, mapper, binds);
    }

}
