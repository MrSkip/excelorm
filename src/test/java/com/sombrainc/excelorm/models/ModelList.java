package com.sombrainc.excelorm.models;

import com.sombrainc.excelorm.annotation.CellCollection;
import com.sombrainc.excelorm.enumeration.CellStrategy;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ModelList {

    @CellCollection(cells = "a12:i15")
    private List<Integer> intsFixed;

    @CellCollection(cells = "a12", strategy = CellStrategy.ROW_UNTIL_NULL)
    private List<Integer> intsRowUntilNull;

    @CellCollection(cells = "a12", strategy = CellStrategy.COLUMN_UNTIL_NULL)
    private List<Integer> intsColUntilNull;

    @CellCollection(cells = "a12", strategy = CellStrategy.COLUMN_UNTIL_NULL, step = 2)
    private List<Integer> intsColUntilNullStep2;

    @CellCollection(cells = "a17:i19")
    private List<Boolean> booleans;

    @CellCollection(cells = "a22:i24")
    private List<BigDecimal> decimals;

}
