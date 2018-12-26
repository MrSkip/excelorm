package com.sombrainc.excelorm.models.modelmap;

import com.sombrainc.excelorm.annotation.CellMap;
import com.sombrainc.excelorm.enumeration.CellStrategy;
import lombok.Data;

import java.util.Map;

@Data
public class MapModel3 {

    @CellMap(keyCell = "B32", strategy = CellStrategy.COLUMN_UNTIL_NULL)
    private Map<String, Object> map;

}
