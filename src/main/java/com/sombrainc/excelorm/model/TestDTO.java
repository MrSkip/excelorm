package com.sombrainc.excelorm.model;

import com.sombrainc.excelorm.annotation.CellMap;
import com.sombrainc.excelorm.annotation.CellPosition;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TestDTO {
//
//    @CellPosition(position = "B2")
//    private String title;
//
//    @CellPosition(position = "C2")
//    private Integer number;

//    @CellPosition(position = "E3:B4")
//    private List<Integer> list;

    @CellMap(keyCell = "B11:B11")
    private Map<String, Student> students;

//    @CellMap(strategy = DataQualifier.ROW_UNTIL_NULL, keyCell = "B11")
//    private Map<String, Student> students;

    @Data
    public static class Student {
        @CellPosition(position = "C11")
        private Integer age;
        @CellPosition(position = "D11")
        private String className;

        @CellPosition(position = "F11:H11")
        private List<Integer> ets;
    }

//    public static class Subject {
//
//    }

}
