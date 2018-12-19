package com.sombrainc.excelorm.model;

import com.sombrainc.excelorm.annotation.Cell;
import com.sombrainc.excelorm.annotation.CellMark;
import com.sombrainc.excelorm.enumeration.DataQualifier;
import lombok.Data;

import java.util.List;

@Data
public class TestDTO {

    @Cell(position = "A2")
    private List<Student> students;

    @Data
    public static class Student {
        @Cell(position = "A2")
        private String name;
        @Cell(position = "B2")
        private Integer age;
        @Cell(position = "C2")
        private String sex;

        @Cell(position = "E2", strategy = DataQualifier.COLUMN_UNTIL_NULL)
        private List<Integer> ets;

        @CellMark
        private Subject subject;
    }

    @Data
    public static class Subject {
        @Cell(position = "j2")
        private Integer age;
    }

}
