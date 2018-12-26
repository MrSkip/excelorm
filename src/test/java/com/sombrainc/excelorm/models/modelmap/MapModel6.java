package com.sombrainc.excelorm.models.modelmap;

import com.sombrainc.excelorm.annotation.CellMap;
import lombok.Data;

import java.util.Map;

@Data
public class MapModel6 {

    @CellMap(keyCell = "B38:H38", step = 3)
    private Map<String, Object> map;

}
