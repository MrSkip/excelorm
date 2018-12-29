package com.sombrainc.excelorm.models.badinput;

import com.sombrainc.excelorm.annotation.CellMap;
import lombok.Data;

import java.util.Map;

@Data
public class IncorrectModel6 {

    @CellMap(keyCell = "A15:C19")
    private Map<String, String> map;

}
