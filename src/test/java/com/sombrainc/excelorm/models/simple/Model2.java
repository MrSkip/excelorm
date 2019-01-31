package com.sombrainc.excelorm.models.simple;

import com.sombrainc.excelorm.annotation.Cell;
import lombok.Data;

@Data
public class Model2 {

    @Cell(value = "a5")
    private int a1;
    @Cell(value = "b5")
    private String b1;
    @Cell(value = "c5")
    private double c1;
    @Cell(value = "d5")
    private boolean d1;

}
