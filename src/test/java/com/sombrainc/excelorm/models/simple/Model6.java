package com.sombrainc.excelorm.models.simple;

import com.sombrainc.excelorm.annotation.CellCollection;
import com.sombrainc.excelorm.enumeration.CellStrategy;
import lombok.Data;

import java.util.List;

@Data
public class Model6 {

    @CellCollection(cells = "a7", strategy = CellStrategy.ROW_UNTIL_NULL)
    private List<String> list;

}
