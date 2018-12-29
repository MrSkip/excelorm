package com.sombrainc.excelorm.models.badinput;

import com.sombrainc.excelorm.annotation.Cell;
import lombok.Data;

@Data
public class IncorrectModel3 {

    @Cell(cell = "r")
    private String name;

}
