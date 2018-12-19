package com.sombrainc.excelorm.model;

import com.sombrainc.excelorm.annotation.Cell;
import com.sombrainc.excelorm.annotation.CellMark;
import lombok.Data;

import java.util.List;

@Data
public class TestDTO {
//
    @Cell(position = "B2")
    private String title;

//
//    @Cell(position = "C2")
//    private Integer number;


//    @CellMap(keyCell = "B11", strategy = DataQualifier.ROW_UNTIL_NULL)
//    private Map<String, Student> students;

    @CellMark
    private Student student;

//    @CellMap(strategy = DataQualifier.ROW_UNTIL_NULL, keyCell = "B11")
//    private Map<String, Student> students;

    @Data
    public static class Student {
        @Cell(position = "C11")
        private Integer age;
        @Cell(position = "D11")
        private String className;

        @Cell(position = "F11:H11")
        private List<Integer> ets;

        @CellMark
        private Subject subject;
    }

    @Data
    public static class Subject {
        @Cell(position = "C11")
        private Integer age;
        @Cell(position = "D11")
        private String className;
    }

}
