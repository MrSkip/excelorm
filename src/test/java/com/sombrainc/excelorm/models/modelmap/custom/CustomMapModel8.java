package com.sombrainc.excelorm.models.modelmap.custom;

import com.sombrainc.excelorm.annotation.Cell;
import com.sombrainc.excelorm.annotation.CellCollection;
import com.sombrainc.excelorm.annotation.CellMap;
import com.sombrainc.excelorm.enumeration.CellStrategy;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CustomMapModel8 {
    @CellMap(keyCell = "A60:A62")
    private Map<Integer, User> map;

    @Data
    public static class User {
        @Cell(value = "B60")
        private String firstName;
        @Cell(value = "C60")
        private String lastName;
        @Cell(value = "D60")
        private Integer age;
        @Cell(value = "E60")
        private Gender gender;
        @CellCollection(cells = "F60", strategy = CellStrategy.COLUMN_UNTIL_NULL)
        private List<Integer> etc;

        public User() {
        }

        public User(String firstName, String lastName, Integer age, Gender gender, List<Integer> etc) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
            this.gender = gender;
            this.etc = etc;
        }
    }

}
