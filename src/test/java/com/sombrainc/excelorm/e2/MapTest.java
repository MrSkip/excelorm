package com.sombrainc.excelorm.e2;

import com.sombrainc.excelorm.e2.utils.EFilters;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.sombrainc.excelorm.utils.ModelReader.executeForE2;

@Test
public class MapTest {
    private static final String DEFAULT_SHEET = "e2Single";

    public void numberAsText() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            Map<String, Integer> value = e2
                    .mapOf(String.class, int.class)
                    .pick("D33:F35", "H37:J39")
                    .filter(EFilters::isNotBlank)
                    .mapValue(cell -> ((int)cell.getNumericCellValue()) * 10)
                    .go();
            Map<String, Integer> expected = new HashMap<>();
            IntStream.rangeClosed(1, 6).forEach(v -> expected.put(v + "", v * 10));
            Assert.assertEquals(value, expected);
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
                    .map(cell -> ((long) cell.getNumericCellValue()) * 10)
                    .until(cell -> cell.getNumericCellValue() > 100)
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
