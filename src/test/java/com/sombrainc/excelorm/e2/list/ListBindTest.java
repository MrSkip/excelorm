package com.sombrainc.excelorm.e2.list;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sombrainc.excelorm.e2.dto.DatesDTO;
import com.sombrainc.excelorm.e2.dto.UserDTO;
import com.sombrainc.excelorm.e2.impl.Bind;
import com.sombrainc.excelorm.e2.utils.EFilters;
import com.sombrainc.excelorm.utils.Comparisons;
import com.sombrainc.excelorm.utils.Jackson;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.sombrainc.excelorm.e2.utils.EFilters.isBlank;
import static com.sombrainc.excelorm.e2.utils.EFilters.isNotBlank;
import static com.sombrainc.excelorm.utils.ModelReader.executeForE2;

@Test
public class ListBindTest {
    private static final String DEFAULT_SHEET = "e2Single";
    private static final String LIST_BIND_SHEET = "listBindTest";

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

    public void undefinedTypesInsideAttachedObject() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            List<DatesDTO> value = e2
                    .listOf(DatesDTO.class)
                    .binds(
                            new Bind("dates", "A78:A79")
                                    .map(field -> LocalDate.parse(field.toText())),
                            new Bind("date", "A78")
                                    .map(field -> LocalDate.parse(field.toText()))
                    )
                    .pick("A78")
                    .go();
            final List<LocalDate> dates = Stream.of(LocalDate.parse("2019-10-10"), LocalDate.parse("2019-10-14"))
                    .collect(Collectors.toList());
            final DatesDTO datesDTO = new DatesDTO().setDate(LocalDate.parse("2019-10-10")).setDates(dates);

            Assert.assertEquals(value, new ArrayList<>(Collections.singleton(datesDTO)));
        });
    }

    public void fieldAsEnum() {
        executeForE2(LIST_BIND_SHEET, e2 -> {
            List<UserDTO> value = e2
                    .listOf(UserDTO.class)
                    .binds(new Bind("testEnum", "A1"))
                    .pick("A1:A3")
                    .go();
            Assert.assertEquals(value, Stream.of("T1", "T2", "T3")
                    .map(s -> new UserDTO().setTestEnum(UserDTO.TestEnum.valueOf(s))).collect(Collectors.toList()));
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
                                    .until(field -> isNotBlank(field) && field.toInt() > 1),
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
                                    .until(field -> isBlank(field.poi()) || field.toInt() > 1)
                                    .map(field -> field.toInt() * 10),
                            new Bind("listOfIntAsStr", "M24:O24")
                                    .until(isBlank())
                                    .map(field -> field.toInt() + "a")
                    )
                    .pick("M24:M29")
                    .filter(isNotBlank())
                    .go();
            Comparisons.compareLists(value, Jackson.parseTo(new TypeReference<List<UserDTO>>() {
            }, "/json/e2/list/customObjectWithTwoListInsideFilterUntilMap.json"));
        });
    }

    public void customObjectWithTwoListInsideFilterUntilMap2() {
        executeForE2(LIST_BIND_SHEET, e2 -> {
            List<UserDTO> value = e2
                    .listOf(UserDTO.class)
                    .binds(
                            new Bind("listOfInt", "B6:E6")
                                    .map(field -> -field.toInt())
                                    .filter(EFilters::isNotBlank)
                    )
                    .pick("D6:D13")
                    .filter(isNotBlank())
                    .until(field -> field.isNotBlank() && field.toInt() == 2)
                    .go();
            Comparisons.compareLists(value, Jackson.parseTo(new TypeReference<List<UserDTO>>() {
            }, "/json/e2/list/customObjectWithTwoListInsideFilterUntilMap2.json"));
        });
    }

}
