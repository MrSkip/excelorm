package com.sombrainc.excelorm.model;

import com.sombrainc.excelorm.annotation.CellMap;
import com.sombrainc.excelorm.annotation.CellPosition;
import com.sombrainc.excelorm.enumeration.DataQualifier;
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

    @CellPosition(position = "E3:B4")
    private List<Integer> list;

    //    @CellMap(strategy = DataQualifier.COLUMN_UNTIL_NULL, keyCell = "E11:F11", firstValueCell = "E12")
//    private Map<String, Student> students;

    @CellMap(strategy = DataQualifier.ROW_UNTIL_NULL, keyCell = "B11")
    private Map<String, Student> students;

    @Data
    public static class Student {
        @CellPosition(position = "C11")
        private Integer age;
        @CellPosition(position = "D11")
        private String className;

        @CellPosition(position = "F11", strategy = DataQualifier.COLUMN_UNTIL_NULL)
        private List<String> ets;
    }

//    public static class Subject {
//
//    }

}
