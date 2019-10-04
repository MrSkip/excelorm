package com.sombrainc.excelorm.e2.map.list;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sombrainc.excelorm.e2.impl.BindField;
import com.sombrainc.excelorm.e2.utils.EFilters;
import com.sombrainc.excelorm.utils.Comparisons;
import com.sombrainc.excelorm.utils.Jackson;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static com.sombrainc.excelorm.e2.utils.EFilters.isNotBlank;
import static com.sombrainc.excelorm.utils.ModelReader.executeForE2;

@Test
public class MapListTest {
    private static final String DEFAULT_SHEET = "e2Single";
    private static final String DEFAULT_MAP_SHEET = "mapList";

    public void _1() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            Map<String, List<Integer>> value = e2
                    .mapOfList(String.class, int.class)
                    .pick("B45:D46", "F45:H46")
                    .filter(EFilters::isNotBlank)
                    .mapValue(cell -> cell.toInt() * 10)
                    .go();
            Map<String, List<Integer>> expected = new HashMap<>();
            IntStream.rangeClosed(1, 6).forEach(v -> expected.put(v + "", new ArrayList<Integer>() {{
                add((v + 9) * 10);
            }}));
            Assert.assertEquals(value, expected);
        });
    }

    public void _2() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            Map<String, List<Integer>> value = e2
                    .mapOfList(String.class, int.class)
                    .pick("B45:D45", "F45:H45")
                    .filter(isNotBlank())
                    .mapValue(cell -> cell.toInt() * 10)
                    .go();
            Map<String, List<Integer>> expected = new HashMap<>();
            IntStream.rangeClosed(1, 3).forEach(v -> expected.put(v + "", new ArrayList<Integer>() {{
                add((v + 9) * 10);
            }}));
            Assert.assertEquals(value, expected);
        });
    }

    public void _3() {
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

    public void _4() {
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

    public void _5() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            Map<String, List<String>> value = e2
                    .mapOfList(String.class, String.class)
                    .pick("B4:B7", "D4:F4")
                    .go();
            Comparisons.compareMaps(value, Jackson.parseTo(new TypeReference<Map<String, List<String>>>() {
            }, "/json/e2/map/list/_5.json"));
        });
    }

    public void _6() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            Map<String, List<Long>> value = e2
                    .mapOfList(String.class, long.class)
                    .pick("B4:B7", "D4:F4")
                    .go();
            Comparisons.compareMaps(value, Jackson.parseTo(new TypeReference<Map<String, List<Long>>>() {
            }, "/json/e2/map/list/_5.json"));
        });
    }

    public void _7() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            Map<String, List<Long>> value = e2
                    .mapOfList(String.class, long.class)
                    .pick("B4:B7", "E4:E7")
                    .go();
            Comparisons.compareMaps(value, Jackson.parseTo(new TypeReference<Map<String, List<Long>>>() {
            }, "/json/e2/map/list/_7.json"));
        });
    }

    public void _8() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            Map<String, List<Long>> value = e2
                    .mapOfList(String.class, long.class)
                    .pick("D4:F4", "B4:B6")
                    .freeze()
                    .go();
            Comparisons.compareMaps(value, Jackson.parseTo(new TypeReference<Map<String, List<Long>>>() {
            }, "/json/e2/map/list/_8.json"));
        });
    }

    public void _9() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            Map<String, List<Long>> value = e2
                    .mapOfList(String.class, long.class)
                    .pick("E10:F10", "B10:B11")
                    .go();
            Comparisons.compareMaps(value, Jackson.parseTo(new TypeReference<Map<String, List<Long>>>() {
            }, "/json/e2/map/list/_9.json"));
        });
    }

    public void _10() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            Map<String, List<Long>> value = e2
                    .mapOfList(String.class, long.class)
                    .pick("E10:F10", "B10:B11")
                    .filter(field -> field.toInt() < 2)
                    .go();
            Comparisons.compareMaps(value, Jackson.parseTo(new TypeReference<Map<String, List<Long>>>() {
            }, "/json/e2/map/list/_10.json"));
        });
    }

    public void _11() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            Map<String, List<Long>> value = e2
                    .mapOfList(String.class, long.class)
                    .pick("E10:F10", "B10:B11")
                    .filter(field -> field.toInt() < 2)
                    .filterValue(field -> field.toInt() < 2)
                    .go();
            Comparisons.compareMaps(value, Jackson.parseTo(new TypeReference<Map<String, List<Long>>>() {
            }, "/json/e2/map/list/_11.json"));
        });
    }

    public void _12() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            Map<String, List<Long>> value = e2
                    .mapOfList(String.class, long.class)
                    .pick("E10:F10", "B10:B11")
                    .filter(field -> field.toInt() < 2)
                    .filterValue(field -> field.toInt() < 2)
                    .map(field -> field.toInt() + 10 + "")
                    .mapValue(field -> field.toLong() * 2)
                    .go();
            Comparisons.compareMaps(value, Jackson.parseTo(new TypeReference<Map<String, List<Long>>>() {
            }, "/json/e2/map/list/_12.json"));
        });
    }

    public void _13() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            Map<String, List<Long>> value = e2
                    .mapOfList(String.class, long.class)
                    .pick("E10:H10", "B10:B11")
                    .filter(field -> field.toInt() < 2)
                    .filterValue(field -> field.toInt() < 2)
                    .map(field -> field.toInt() + 10 + "")
                    .mapValue(field -> field.toLong() * 2)
                    .until(BindField::isBlank)
                    .go();
            Comparisons.compareMaps(value, Jackson.parseTo(new TypeReference<Map<String, List<Long>>>() {
            }, "/json/e2/map/list/_12.json"));
        });
    }

    public void _14() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            Map<String, List<Long>> value = e2
                    .mapOfList(String.class, long.class)
                    .pick("E10:H10", "B10:B11")
                    .filter(field -> field.toInt() < 2)
                    .filterValue(field -> field.toInt() < 2)
                    .map(field -> field.toInt() + 10 + "")
                    .mapValue(field -> field.toLong() * 2)
                    .until(BindField::isBlank)
                    .freeze()
                    .go();
            Comparisons.compareMaps(value, Jackson.parseTo(new TypeReference<Map<String, List<Long>>>() {
            }, "/json/e2/map/list/_12.json"));
        });
    }

}
