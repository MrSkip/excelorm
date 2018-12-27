package com.sombrainc.excelorm.models.modelmap.custom;

import com.sombrainc.excelorm.annotation.Cell;
import com.sombrainc.excelorm.annotation.CellMap;
import com.sombrainc.excelorm.enumeration.CellStrategy;
import lombok.Data;

import java.util.Map;

@Data
public class CustomMapModel7 {
    @CellMap(keyCell = "B67", strategy = CellStrategy.COLUMN_UNTIL_NULL, step = 3)
    private Map<Integer, User> map;

    @Data
    public static class User {
        @Cell(cell = "B68")
        private String firstName;
        @Cell(cell = "B69")
        private String lastName;
        @Cell(cell = "B70")
        private Integer age;
        @Cell(cell = "B71")
        private Gender gender;

        public User() {
        }

        public User(String firstName, String lastName, Integer age, Gender gender) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
            this.gender = gender;
        }
    }

}
