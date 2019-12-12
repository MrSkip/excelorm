package com.sombrainc.excelorm.e2.list;

import com.sombrainc.excelorm.e2.utils.EFilters;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.util.Strings;

import java.time.LocalDate;
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

    public void undefinedTypeInsideList() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            List<LocalDate> value = e2
                    .listOf(LocalDate.class)
                    .pick("a63")
                    .map(field -> LocalDate.parse(field.toText()))
                    .go();
            Assert.assertEquals(value, Stream.of(LocalDate.parse("2019-10-10")).collect(Collectors.toList()));
        });
    }

    public void undefinedTypesInsideList() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            List<LocalDate> value = e2
                    .listOf(LocalDate.class)
                    .pick("a78:a80")
                    .map(field -> LocalDate.parse(field.toText()))
                    .go();
            final List<LocalDate> dates = Stream.of(
                    LocalDate.parse("2019-10-10"),
                    LocalDate.parse("2019-10-14"),
                    LocalDate.parse("2019-10-12")
            ).collect(Collectors.toList());
            Assert.assertEquals(value, dates);
        });
    }

    public void allowNullInsideList() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            List<String> value = e2
                    .listOf(String.class)
                    .pick("g3")
                    .map(field -> Strings.isNullOrEmpty(field.toText()) ? null : field.toText())
                    .go();
            Assert.assertEquals(value, new ArrayList<>(Collections.singleton(null)));
        });
    }

    public void empty() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            List<String> value = e2
                    .listOf(String.class)
                    .pick("B8")
                    .filter(EFilters.isBlank())
                    .go();
            Assert.assertEquals(value, new ArrayList<>());
        });
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
