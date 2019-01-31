package com.sombrainc.excelorm.models.modelmap.custom;

import com.sombrainc.excelorm.annotation.Cell;
import lombok.Data;

@Data
public class User {
    @Cell(value = "B60")
    private String firstName;
    @Cell(value = "C60")
    private String lastName;
    @Cell(value = "D60")
    private Integer age;
    @Cell(value = "E60")
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
