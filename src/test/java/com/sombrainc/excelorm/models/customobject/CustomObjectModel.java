package com.sombrainc.excelorm.models.customobject;

import com.sombrainc.excelorm.annotation.Cell;
import com.sombrainc.excelorm.annotation.CellMap;
import com.sombrainc.excelorm.annotation.CellMark;
import com.sombrainc.excelorm.enumeration.CellStrategy;
import com.sombrainc.excelorm.models.modelmap.custom.Gender;
import lombok.Data;

import java.util.Map;

@Data
public class CustomObjectModel {

    @CellMark
    private User user;

    @Data
    public static class User {
        @Cell(value = "C75")
        private String firstName;
        @Cell(value = "C76")
        private String lastName;
        @Cell(value = "C77")
        private Integer age;
        @Cell(value = "C78")
        private Gender gender;
        @CellMap(keyCell = "E75", strategy = CellStrategy.ROW_UNTIL_NULL)
        private Map<String, Integer> map;

        public User() {
        }

        public User(String firstName, String lastName, Integer age, Gender gender, Map<String, Integer> map) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
            this.gender = gender;
            this.map = map;
        }
    }

}
