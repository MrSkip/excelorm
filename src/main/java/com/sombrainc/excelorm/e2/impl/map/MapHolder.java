package com.sombrainc.excelorm.e2.impl.map;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.poi.ss.usermodel.Cell;

import java.util.function.Function;

@Data
@Accessors(chain = true)
public class MapHolder<K1, V1> {
    private Class<K1> keyClass;
    private Class<V1> valueClass;

    private Function<Cell, K1> keyMapper;
    private Function<Cell, V1> valueMapper;

    private Function<Cell, Boolean> keyUntil;
    private Function<Cell, Boolean> valueUntil;

    private Function<Cell, Boolean> keyFilter;
    private Function<Cell, Boolean> valueFilter;

    protected String keyRange;
    private String valueRange;
}
