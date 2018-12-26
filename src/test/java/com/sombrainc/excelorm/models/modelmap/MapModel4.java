package com.sombrainc.excelorm.models.modelmap;

import com.sombrainc.excelorm.annotation.CellMap;
import com.sombrainc.excelorm.enumeration.CellStrategy;
import lombok.Data;

import java.util.Map;

@Data
public class MapModel4 {

    @CellMap(keyCell = "B32:D32", valueCell = "B35")
    private Map<String, Object> map;

}
