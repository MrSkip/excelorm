package com.sombrainc.excelorm.models.modelmap.custom;

import com.sombrainc.excelorm.annotation.CellMap;
import com.sombrainc.excelorm.enumeration.CellStrategy;
import lombok.Data;

import java.util.Map;

@Data
public class CustomMapModel4 {
    @CellMap(keyCell = "A60", strategy = CellStrategy.ROW_UNTIL_NULL, step = 2)
    private Map<Integer, User> map;
}
