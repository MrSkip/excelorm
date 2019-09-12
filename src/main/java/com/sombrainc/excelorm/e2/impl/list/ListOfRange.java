package com.sombrainc.excelorm.e2.impl.list;

import com.sombrainc.excelorm.e2.impl.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class ListOfRange<T> extends CoreActions<List<T>> {
    protected Function<BindField, T> mapper;
    protected Function<BindField, Boolean> filter;
    protected Function<BindField, Boolean> until;
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

    /**
     * Used to specify user object
     *
     * @param binds provided array of user objects to be mapped
     * @return set of other functions
     */
    public ListOfRange<T> binds(Bind... binds) {
        this.binds = Arrays.asList(Objects.requireNonNull(binds));
        return new ListOfRange<>(getEReaderContext(), this);
    }

    /**
     * Specify range or cell on the spreadsheet
     *
     * @param range the range of cells or cell address in the following format:
     *              `A1`, `a1`, `A1:A2`, `a1:a2`, `A1:B5`, `a1:b5`
     * @return set of other functions
     */
    public ListOfRange<T> pick(String range) {
        this.range = range;
        return new ListOfRange<>(getEReaderContext(), this);
    }

    /**
     * Custom mapper for items
     *
     * @param mapper mapper function
     * @return set of other functions
     */
    public ListOfRange<T> map(Function<BindField, T> mapper) {
        this.mapper = mapper;
        return new ListOfRange<>(this.getEReaderContext(), this);
    }

    /**
     * Build a filter
     *
     * @param filter filter function
     * @return set of other functions
     */
    public ListOfRange<T> filter(Function<BindField, Boolean> filter) {
        this.filter = filter;
        return new ListOfRange<>(this.getEReaderContext(), this);
    }

    /**
     * Iterate until some special condition
     *
     * @param until specify the condition
     * @return set of other functions
     */
    public ListOfRange<T> until(Function<BindField, Boolean> until) {
        this.until = until;
        return new ListOfRange<>(this.getEReaderContext(), this);
    }

}
