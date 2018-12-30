package com.sombrainc.excelorm.models.simple;

import com.sombrainc.excelorm.annotation.Cell;
import com.sombrainc.excelorm.annotation.CellCollection;
import com.sombrainc.excelorm.annotation.CellMark;
import com.sombrainc.excelorm.enumeration.CellStrategy;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class Invoice {

    @Cell(cell = "A1")
    private String companyName;
    @Cell(cell = "A2")
    private String streetAddress;
    @Cell(cell = "A3")
    private String city;
    @Cell(cell = "A4")
    private String phone;
    @Cell(cell = "A5")
    private String fax;
    @Cell(cell = "A6")
    private String website;

    // same mapping for the rest pure fields ..

    @CellMark
    private Bill billTo;
    // will be iterating down to rows until first empty cell (A17, A18 ... n) is found
    @CellCollection(cells = "A17", strategy = CellStrategy.ROW_UNTIL_NULL)
    private List<Expenses> expenses;
    @CellCollection(cells = "A36", strategy = CellStrategy.ROW_UNTIL_NULL)
    private List<String> comments;

    @Data
    public static class Bill {
        @Cell(cell = "A10")
        private String name;
        @Cell(cell = "A11")
        private String companyName;
        @Cell(cell = "A12")
        private String streetAddress;
        @Cell(cell = "A13")
        private String city;
        @Cell(cell = "A14")
        private String phone;
    }

    @Data
    public static class Expenses {
        @Cell(cell = "A17")
        private String description;
        @Cell(cell = "E17")
        private Boolean taxed;
        @Cell(cell = "F17")
        private BigDecimal amount;
    }

}
