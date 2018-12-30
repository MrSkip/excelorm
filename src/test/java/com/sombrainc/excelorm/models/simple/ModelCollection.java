package com.sombrainc.excelorm.models.simple;

import com.sombrainc.excelorm.annotation.Cell;
import com.sombrainc.excelorm.annotation.CellCollection;
import com.sombrainc.excelorm.enumeration.CellStrategy;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
public class ModelCollection {

    @CellCollection(cells = "a12:i15")
    private List<Integer> intsFixed;

    @CellCollection(cells = "a12", strategy = CellStrategy.ROW_UNTIL_NULL)
    private List<Integer> intsRowUntilNull;

    @CellCollection(cells = "a12", strategy = CellStrategy.COLUMN_UNTIL_NULL)
    private List<Integer> intsColUntilNull;

    @CellCollection(cells = "a12", strategy = CellStrategy.COLUMN_UNTIL_NULL, step = 2)
    private List<Integer> intsColUntilNullStep2;

    @CellCollection(cells = "A17:I19")
    private List<Boolean> booleans;

    @CellCollection(cells = "a22:i24")
    private List<BigDecimal> decimals;

    @CellCollection(cells = "a22:i24")
    private Set<BigDecimal> decimalsSet;

    @CellCollection(cells = "a22:i24")
    private Collection<BigDecimal> decimalsCollection;

    @CellCollection(cells = "a28:a30")
    private List<Person> people;

    @CellCollection(cells = "a28", strategy = CellStrategy.ROW_UNTIL_NULL)
    private List<Person> people2;

    @Data
    public static class Person {
        @Cell(cell = "a28")
        private String firstName;
        @Cell(cell = "b28")
        private String lastName;
        @Cell(cell = "c28")
        private int age;
        @Cell(cell = "d28")
        private Gender gender;

        public Person() {
        }

        public Person(String firstName, String lastName, int age, Gender gender) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
            this.gender = gender;
        }
    }

    public enum Gender {
        FEMALE,
        MALE
    }

}
