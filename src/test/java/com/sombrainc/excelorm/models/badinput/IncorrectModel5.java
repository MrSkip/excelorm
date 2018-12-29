package com.sombrainc.excelorm.models.badinput;

import com.sombrainc.excelorm.annotation.Cell;
import com.sombrainc.excelorm.annotation.CellCollection;
import lombok.Data;

import java.util.Map;

@Data
public class IncorrectModel5 {

    @CellCollection(cells = "A15:B19")
    private Map<String, String> map;

}
