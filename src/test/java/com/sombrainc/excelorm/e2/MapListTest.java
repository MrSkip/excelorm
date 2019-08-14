package com.sombrainc.excelorm.e2;

import com.sombrainc.excelorm.e2.utils.EFilters;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.sombrainc.excelorm.utils.ModelReader.executeForE2;

@Test
public class MapListTest {
    private static final String DEFAULT_SHEET = "e2Single";

    public void sameVector() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            Map<String, List<Integer>> value = e2
                    .mapOfList(String.class, int.class)
                    .pick("B45:D46", "F45:H46")
                    .filter(EFilters::isNotBlank)
                    .mapValue(cell -> ((int) cell.getNumericCellValue()) * 10)
                    .go();
            Map<String, List<Integer>> expected = new HashMap<>();
            IntStream.rangeClosed(1, 6).forEach(v -> expected.put(v + "", new ArrayList<Integer>() {{
                add((v + 9) * 10);
            }}));
            Assert.assertEquals(value, expected);
        });
    }

    public void sameVector2() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            Map<String, List<Integer>> value = e2
                    .mapOfList(String.class, int.class)
                    .pick("B45:D45", "F45:H45")
                    .filter(EFilters::isNotBlank)
                    .mapValue(cell -> ((int) cell.getNumericCellValue()) * 10)
                    .go();

            Map<String, List<Integer>> expected = new HashMap<>();
            IntStream.rangeClosed(1, 3).forEach(v -> expected.put(v + "", new ArrayList<Integer>() {{
                add((v + 9) * 10);
            }}));
            Assert.assertEquals(value, expected);
        });
    }

    public void horizontalKeysValuesAreVertical() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            Map<String, List<Integer>> value = e2
                    .mapOfList(String.class, int.class)
                    .pick("B45:D45", "B48:B50")
                    .go();
            Map<String, List<Integer>> expected = new HashMap<>();
            IntStream.rangeClosed(1, 3).forEach(v -> expected.put(v + "", new ArrayList<Integer>() {{
                add(v);
                add(v);
                add(v);
            }}));
            Assert.assertEquals(value, expected);
        });
    }

    public void verticalKeysValuesAreHorizontal() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            Map<String, List<Integer>> value = e2
                    .mapOfList(String.class, int.class)
                    .filterValue(EFilters::isNotBlank)
                    .pick("C53:C55", "D53:I53")
                    .go();
            Map<String, List<Integer>> expected = new HashMap<>();
            IntStream.rangeClosed(1, 3).forEach(v -> expected.put(v + "", new ArrayList<Integer>() {{
                add(v);
                add(v);
                add(v);
            }}));
            Assert.assertEquals(value, expected);
        });
    }

}
