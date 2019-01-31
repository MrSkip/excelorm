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

    @Cell(value = "A1")
    private String companyName;
    @Cell(value = "A2")
    private String streetAddress;
    @Cell(value = "A3")
    private String city;
    @Cell(value = "A4")
    private String phone;
    @Cell(value = "A5")
    private String fax;
    @Cell(value = "A6")
    private String website;

    // same mapping for the rest pure fields ..

    @CellMark
    private Bill billTo;
    // will be iterating down to rows until first empty value (A17, A18 ... n) is found
    @CellCollection(cells = "A17", strategy = CellStrategy.ROW_UNTIL_NULL)
    private List<Expenses> expenses;
    @CellCollection(cells = "A36", strategy = CellStrategy.ROW_UNTIL_NULL)
    private List<String> comments;

    @Data
    public static class Bill {
        @Cell(value = "A10")
        private String name;
        @Cell(value = "A11")
        private String companyName;
        @Cell(value = "A12")
        private String streetAddress;
        @Cell(value = "A13")
        private String city;
        @Cell(value = "A14")
        private String phone;
    }

    @Data
    public static class Expenses {
        @Cell(value = "A17")
        private String description;
        @Cell(value = "E17")
        private Boolean taxed;
        @Cell(value = "F17")
        private BigDecimal amount;
    }

}
