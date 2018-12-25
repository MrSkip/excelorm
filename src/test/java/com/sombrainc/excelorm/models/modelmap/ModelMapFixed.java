package com.sombrainc.excelorm.models.modelmap;

import com.sombrainc.excelorm.annotation.CellMap;
import lombok.Data;

import java.util.Map;

@Data
public class ModelMapFixed {

    @CellMap(keyCell = "A33")
    private Map<Integer, String> map;

}
