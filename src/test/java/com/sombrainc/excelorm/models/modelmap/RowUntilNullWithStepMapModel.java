package com.sombrainc.excelorm.models.modelmap;

import com.sombrainc.excelorm.annotation.CellMap;
import com.sombrainc.excelorm.enumeration.CellStrategy;
import lombok.Data;

import java.util.Map;

@Data
public class RowUntilNullWithStepMapModel {

    @CellMap(keyCell = "A33", strategy = CellStrategy.ROW_UNTIL_NULL, step = 2)
    private Map<Integer, String> map;

}
