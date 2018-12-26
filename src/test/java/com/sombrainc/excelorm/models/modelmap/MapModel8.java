package com.sombrainc.excelorm.models.modelmap;

import com.sombrainc.excelorm.annotation.CellMap;
import lombok.Data;

import java.util.Map;

@Data
public class MapModel8 {

    @CellMap(keyCell = "B50:B56", valueCell = "f50", step = 3)
    private Map<String, Object> map;

}
