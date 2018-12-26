package com.sombrainc.excelorm.models.modelmap;

import com.sombrainc.excelorm.annotation.CellMap;
import lombok.Data;

import java.util.Map;

@Data
public class MapModel7 {

    @CellMap(keyCell = "B42:H42", valueCell = "B47", step = 3)
    private Map<String, Object> map;

}
