package com.sombrainc.excelorm.models.simple;

import com.sombrainc.excelorm.annotation.Cell;
import com.sombrainc.excelorm.annotation.CellMark;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class NestedClassModel {

    @CellMark
    private NestedClass nestedClass;

    @Data
    public static class NestedClass {
        @Cell(value = "a1")
        private int a1;
        @Cell(value = "b1")
        private String b1;
        @Cell(value = "c1")
        private double c1;
        @Cell(value = "d1")
        private boolean d1;

        @Cell(value = "d1")
        private Boolean d1BoolObject;

        @Cell(value = "a1")
        private BigDecimal a1BigDecimal;
        @Cell(value = "c1")
        private BigDecimal c1BigDecimal;

        @Cell(value = "c1")
        private Float c1Float;
        @Cell(value = "c1")
        private float c1FloatPrimitive;
    }
}
