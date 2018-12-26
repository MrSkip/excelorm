package com.sombrainc.excelorm.models.modelmap;

import com.sombrainc.excelorm.annotation.CellMap;
import lombok.Data;

import java.util.Map;

@Data
public class MapModel5 {

    @CellMap(keyCell = "B32:E32", valueCell = "B35", step = 2)
    private Map<String, Object> map;

}
