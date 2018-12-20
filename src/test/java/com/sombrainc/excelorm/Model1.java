package com.sombrainc.excelorm;

import com.sombrainc.excelorm.annotation.Cell;
import lombok.Data;

@Data
public class Model1 {

    @Cell(cell = "a1")
    private int a1;
    @Cell(cell = "b1")
    private String b1;
    @Cell(cell = "c1")
    private double c1;
    @Cell(cell = "d1")
    private boolean d1;

}
