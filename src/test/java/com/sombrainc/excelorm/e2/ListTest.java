package com.sombrainc.excelorm.e2;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sombrainc.excelorm.e2.dto.UserDTO;
import com.sombrainc.excelorm.e2.impl.Bind;
import com.sombrainc.excelorm.e2.utils.EFilters;
import com.sombrainc.excelorm.utils.Comparisons;
import com.sombrainc.excelorm.utils.Jackson;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.sombrainc.excelorm.e2.utils.EFilters.isNotBlank;
import static com.sombrainc.excelorm.utils.ModelReader.executeForE2;

@Test
public class ListTest {
    private static final String DEFAULT_SHEET = "e2Single";

    public void numberAsText() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            List<String> value = e2
                    .listOf(String.class)
                    .pick("B8")
                    .go();
            Assert.assertEquals(value, Stream.of("1").collect(Collectors.toList()));
        });
    }

    public void numberAsTextObject() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            List<UserDTO> value = e2
                    .listOf(UserDTO.class)
                    .binds(new Bind("name", "C20"))
                    .pick("C20:E20")
                    .go();
            Assert.assertEquals(value, Stream.of("Test", "Test2", "Test3")
                    .map(s -> new UserDTO().setName(s)).collect(Collectors.toList()));
        });
    }

    public void customObjectWithTwoListInsideFilter() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            List<UserDTO> value = e2
                    .listOf(UserDTO.class)
                    .binds(
                            new Bind("listOfInt", "M24:O24").filter(isNotBlank()),
                            new Bind("listOfIntAsStr", "M24:O24").filter(isNotBlank())
                    )
                    .pick("M24:M29")
                    .filter(EFilters::isNotBlank)
                    .go();
            Comparisons.compareLists(value, Jackson.parseTo(new TypeReference<List<UserDTO>>() {
            }, "/json/e2/list/customObjectWithTwoListInside.json"));
        });
    }

    public void customObjectWithTwoListInsideFilterUntil() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            List<UserDTO> value = e2
                    .listOf(UserDTO.class)
                    .binds(
                            new Bind("listOfInt", "M24:O24")
                                    .filter(isNotBlank())
                                    .until(field -> isNotBlank(field) && field.toInt() <= 1),
                            new Bind("listOfIntAsStr", "M24:O24")
                                    .filter(isNotBlank())
                    )
                    .pick("M24:M29")
                    .filter(isNotBlank())
                    .go();
            Comparisons.compareLists(value, Jackson.parseTo(new TypeReference<List<UserDTO>>() {
            }, "/json/e2/list/customObjectWithTwoListInsideFilterUntil.json"));
        });
    }

    public void customObjectWithTwoListInsideFilterUntilMap() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            List<UserDTO> value = e2
                    .listOf(UserDTO.class)
                    .binds(
                            new Bind("listOfInt", "M24:O24")
                                    .until(field -> isNotBlank(field) && field.toInt() <= 1)
                                    .map(field -> field.toInt() * 10),
                            new Bind("listOfIntAsStr", "M24:O24")
                                    .until(isNotBlank())
                                    .map(field -> field.toInt() + "a")
                    )
                    .pick("M24:M29")
                    .filter(isNotBlank())
                    .go();
            Comparisons.compareLists(value, Jackson.parseTo(new TypeReference<List<UserDTO>>() {
            }, "/json/e2/list/customObjectWithTwoListInsideFilterUntilMap.json"));
        });
    }

    public static void main(String[] args) {
        Stream.of(1,2,3,4,5,6).filter(integer -> integer > 3).forEach(System.out::println);
    }

    public void integer() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            List<Integer> value = e2
                    .listOf(Integer.class)
                    .pick("B8")
                    .go();
            Assert.assertEquals(value, Stream.of(1).collect(Collectors.toList()));
        });
    }

    public void string() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            List<String> value = e2
                    .listOf(String.class)
                    .pick("C20:E20")
                    .go();
            Assert.assertEquals(value, Stream.of("Test", "Test2", "Test3").collect(Collectors.toList()));
        });
    }

    public void longWithMapperUntilFilter() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            List<Long> value = e2
                    .listOf(Long.class)
                    .filter(EFilters::isNotBlank)
                    .map(cell -> cell.toLong() * 10)
                    .until(cell -> isNotBlank(cell) && cell.toInt() > 100)
                    .pick("C25:J25")
                    .go();
            Assert.assertEquals(value, Stream.of(10L, 20L, 30L, 40L).collect(Collectors.toList()));
        });
    }

    public void longFilter() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            List<Integer> value = e2
                    .listOf(int.class)
                    .filter(EFilters::isNotBlank)
                    .pick("C24:L26")
                    .go();
            Assert.assertEquals(value, Stream.of(1, 2, 3, 4, 123, 5).collect(Collectors.toList()));
        });
    }

}
