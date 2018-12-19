package com.sombrainc.excelorm;

import com.sombrainc.excelorm.annotation.Cell;
import lombok.Data;

@Data
public class Model2 {

    @Cell(position = "a5")
    private int a1;
    @Cell(position = "b5")
    private String b1;
    @Cell(position = "c5")
    private double c1;
    @Cell(position = "d5")
    private boolean d1;

}
