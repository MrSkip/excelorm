package com.sombrainc.excelorm.models.badinput;

import com.sombrainc.excelorm.annotation.Cell;
import lombok.Data;

@Data
public class IncorrectModel2 {

    @Cell(value = "^")
    private String name;

}
