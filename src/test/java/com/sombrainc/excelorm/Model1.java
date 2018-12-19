package com.sombrainc.excelorm;

import com.sombrainc.excelorm.annotation.Cell;
import lombok.Data;

@Data
public class Model1 {

    @Cell(position = "a1")
    private int a1;
    @Cell(position = "b1")
    private String b1;
    @Cell(position = "c1")
    private double c1;
    @Cell(position = "d1")
    private boolean d1;

}
