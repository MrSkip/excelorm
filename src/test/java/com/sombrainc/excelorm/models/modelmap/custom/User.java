package com.sombrainc.excelorm.models.modelmap.custom;

import com.sombrainc.excelorm.annotation.Cell;
import lombok.Data;

@Data
public class User {
    @Cell(cell = "B60")
    private String firstName;
    @Cell(cell = "C60")
    private String lastName;
    @Cell(cell = "D60")
    private Integer age;
    @Cell(cell = "E60")
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
