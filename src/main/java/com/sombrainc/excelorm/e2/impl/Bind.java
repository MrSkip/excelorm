package com.sombrainc.excelorm.e2.impl;

import com.sombrainc.excelorm.utils.StringUtils;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.poi.ss.usermodel.Cell;

import java.util.Objects;
import java.util.function.Function;

@Data
@Accessors(chain = true)
public class Bind {
    private final String field;
    private final String cell;

    private Function<Cell, Boolean> filter;
    private Function<Cell, Object> mapper;
    private Function<Cell, Boolean> until;

    public Bind(String field, String cell) {
        this.field = StringUtils.requireNotBlank(field);
        this.cell = StringUtils.requireNotBlank(cell);
    }

    public Bind filter(final Function<Cell, Boolean> filter) {
        this.filter = filter;
        return this;
    }

    public Bind map(final Function<Cell, Object> mapper) {
        this.mapper = mapper;
        return this;
    }

    public Bind until(final Function<Cell, Boolean> until) {
        this.until = until;
        return this;
    }

}
