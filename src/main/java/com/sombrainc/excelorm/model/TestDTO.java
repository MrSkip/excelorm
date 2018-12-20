package com.sombrainc.excelorm.model;

import com.sombrainc.excelorm.annotation.Cell;
import com.sombrainc.excelorm.annotation.CellCollection;
import com.sombrainc.excelorm.annotation.CellMap;
import com.sombrainc.excelorm.annotation.CellMark;
import com.sombrainc.excelorm.enumeration.CellStrategy;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TestDTO {

    @CellCollection(cells = "A2", strategy = CellStrategy.COLUMN_UNTIL_NULL, step = 2)
    private List<Student> students;

    @CellMap(keyCell = "B4", step = 2)
    private Map<String, Student> map;

    @Cell(cell = "A3")
    private String name;

    @CellMark
    private Subject subject;

    @Data
    public static class Student {
        @Cell(cell = "A2")
        private String name;
        @Cell(cell = "B2")
        private Integer age;
        @Cell(cell = "C2")
        private String sex;

        @CellCollection(cells = "E2", strategy = CellStrategy.COLUMN_UNTIL_NULL)
        private List<Integer> ets;

        @CellMark
        private Subject subject;
    }

    @Data
    public static class Subject {
        @Cell(cell = "j2")
        private Integer age;
    }

}
