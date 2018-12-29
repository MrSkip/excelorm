package com.sombrainc.excelorm.models.badinput;

import com.sombrainc.excelorm.annotation.Cell;
import lombok.Data;

@Data
public class IncorrectModel1 {

    @Cell(cell = "")
    private String name;

}
