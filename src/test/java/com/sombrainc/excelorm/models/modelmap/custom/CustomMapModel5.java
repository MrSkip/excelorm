package com.sombrainc.excelorm.models.modelmap.custom;

import com.sombrainc.excelorm.annotation.CellMap;
import com.sombrainc.excelorm.enumeration.CellStrategy;
import lombok.Data;

import java.util.Map;

@Data
public class CustomMapModel5 {
    @CellMap(keyCell = "A60:A61", step = 2)
    private Map<Integer, User> map;
}
