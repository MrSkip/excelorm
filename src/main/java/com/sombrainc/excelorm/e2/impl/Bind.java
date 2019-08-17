package com.sombrainc.excelorm.e2.impl;

import com.sombrainc.excelorm.utils.StringUtils;
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

    public Bind(String field, String initialCell) {
        this.field = StringUtils.requireNotBlank(field);
        this.initialCell = StringUtils.requireNotBlank(initialCell);
    }

    public Bind filter(final Function<BindField, Boolean> filter) {
        this.filter = filter;
        return this;
    }

    public Bind map(final Function<BindField, Object> mapper) {
        this.mapper = mapper;
        return this;
    }

    public Bind until(final Function<BindField, Boolean> until) {
        this.until = until;
        return this;
    }

}
