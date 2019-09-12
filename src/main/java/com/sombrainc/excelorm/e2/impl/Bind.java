package com.sombrainc.excelorm.e2.impl;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.function.Function;

@Data
@Accessors(chain = true)
public class Bind {
    private final String field;
    private final String initialCell;

    private Function<BindField, Boolean> filter;
    private Function<BindField, Object> mapper;
    private Function<BindField, Boolean> until;

    public Bind(String field, String cell) {
        this.field = field;
        this.initialCell = cell;
    }

    /**
     * Build a filter
     *
     * @param filter filter function
     * @return set of other functions
     */
    public Bind filter(final Function<BindField, Boolean> filter) {
        this.filter = filter;
        return this;
    }

    /**
     * Custom mapper for items
     *
     * @param mapper mapper function
     * @return set of other functions
     */
    public Bind map(final Function<BindField, Object> mapper) {
        this.mapper = mapper;
        return this;
    }

    /**
     * Iterate until some special condition
     *
     * @param until specify the condition
     * @return set of other functions
     */
    public Bind until(final Function<BindField, Boolean> until) {
        this.until = until;
        return this;
    }

}
