package com.sombrainc.excelorm.models.badinput;

import com.sombrainc.excelorm.annotation.Cell;
import lombok.Data;

import java.util.Map;

@Data
public class IncorrectModel4 {

    @Cell(value = "A15:B19")
    private Map<String, String> map;

}
