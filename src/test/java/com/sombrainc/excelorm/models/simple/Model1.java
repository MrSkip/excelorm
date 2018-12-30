package com.sombrainc.excelorm.models.simple;

import com.sombrainc.excelorm.annotation.Cell;
import lombok.Data;

import java.math.BigDecimal;

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

    @Cell(cell = "d1")
    private Boolean d1BoolObject;

    @Cell(cell = "a1")
    private BigDecimal a1BigDecimal;
    @Cell(cell = "c1")
    private BigDecimal c1BigDecimal;

    @Cell(cell = "c1")
    private Float c1Float;
    @Cell(cell = "c1")
    private float c1FloatPrimitive;
}
