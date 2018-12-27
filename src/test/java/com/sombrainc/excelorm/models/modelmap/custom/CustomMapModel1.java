package com.sombrainc.excelorm.models.modelmap.custom;

import com.sombrainc.excelorm.annotation.CellMap;
import lombok.Data;

import java.util.Map;

@Data
public class CustomMapModel1 {
    @CellMap(keyCell = "A60")
    private Map<Integer, User> map;

}
