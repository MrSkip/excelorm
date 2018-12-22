package com.sombrainc.excelorm.models;

import com.sombrainc.excelorm.annotation.Cell;
import lombok.Data;

@Data
public class Model2 {

    @Cell(cell = "a5")
    private int a1;
    @Cell(cell = "b5")
    private String b1;
    @Cell(cell = "c5")
    private double c1;
    @Cell(cell = "d5")
    private boolean d1;

}
