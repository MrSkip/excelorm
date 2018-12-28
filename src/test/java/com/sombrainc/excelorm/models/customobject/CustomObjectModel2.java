package com.sombrainc.excelorm.models.customobject;

import com.sombrainc.excelorm.annotation.Cell;
import com.sombrainc.excelorm.annotation.CellMap;
import com.sombrainc.excelorm.annotation.CellMark;
import com.sombrainc.excelorm.enumeration.CellStrategy;
import lombok.Data;

import java.util.Map;

@Data
public class CustomObjectModel2 {

    @CellMark
    private Lesson lesson;

    public CustomObjectModel2() {
    }

    public CustomObjectModel2(Lesson lesson) {
        this.lesson = lesson;
    }

    @Data
    public static class Lesson {

        @CellMap(keyCell = "C82", strategy = CellStrategy.ROW_UNTIL_NULL)
        private Map<String, Subject> subjects;

        public Lesson() {
        }

        public Lesson(Map<String, Subject> subjects) {
            this.subjects = subjects;
        }
    }

    @Data
    public static class Subject {
//        @Cell(cell = "D82")
        private Integer hours;
        @CellMark
        private Student student;
//        @Cell(cell = "H82")
        private Double sum;

        public Subject() {
        }

        public Subject(Integer hours, Student student, Double sum) {
            this.hours = hours;
            this.student = student;
            this.sum = sum;
        }
    }

    @Data
    public static class Student {
        @CellMap(keyCell = "E81:G81")
        private Map<String, Integer> students;

        public Student() {
        }

        public Student(Map<String, Integer> students) {
            this.students = students;
        }
    }

}
