package com.sombrainc.excelorm.e2.list;

import com.sombrainc.excelorm.e2.utils.EFilters;
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
