package com.sombrainc.excelorm.e2.map.range;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sombrainc.excelorm.e2.impl.BindField;
import com.sombrainc.excelorm.e2.utils.EFilters;
import com.sombrainc.excelorm.utils.Jackson;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import static com.sombrainc.excelorm.e2.utils.EFilters.isNotBlank;
import static com.sombrainc.excelorm.utils.ModelReader.executeForE2;

@Test
public class MapTest {
    private static final String DEFAULT_SHEET = "e2Single";
    private static final String DEFAULT_MAP_SHEET = "e2Map";

    public void _1() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            Map<String, Integer> value = e2
                    .mapOf(String.class, int.class)
                    .pick("D33:F35", "H37:J39")
                    .filter(EFilters::isNotBlank)
                    .mapValue(cell -> cell.toInt() * 10)
                    .go();
            Map<String, Integer> expected = new HashMap<>();
            IntStream.rangeClosed(1, 6).forEach(v -> expected.put(v + "", v * 10));
            Assert.assertEquals(value, expected);
        });
    }

    public void _2() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            Map<Integer, String> value = e2
                    .mapOf(int.class, String.class)
                    .pick("B3:B7", "D11:D15")
                    .go();
            Assert.assertEquals(value, Jackson.parseTo(new TypeReference<Map<Integer, String>>() {
            }, "/json/e2/map/range/_2.json"));
        });
    }

    public void _3() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            Map<Integer, String> value = e2
                    .mapOf(int.class, String.class)
                    .pick("B3:B7", "D11:D15")
                    .go();
            Assert.assertEquals(value, Jackson.parseTo(new TypeReference<Map<Integer, String>>() {
            }, "/json/e2/map/range/_2.json"));
        });
    }

    public void _4() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            Map<Integer, String> value = e2
                    .mapOf(int.class, String.class)
                    .pick("B3:B7", "D11")
                    .go();
            Assert.assertEquals(value, Jackson.parseTo(new TypeReference<Map<Integer, String>>() {
            }, "/json/e2/map/range/_2.json"));
        });
    }

    public void _5() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            Map<Integer, String> value = e2
                    .mapOf(int.class, String.class)
                    .pick("B20:f20", "e23")
                    .go();
            Assert.assertEquals(value, Jackson.parseTo(new TypeReference<Map<Integer, String>>() {
            }, "/json/e2/map/range/_2.json"));
        });
    }

    public void _6() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            Map<Integer, String> value = e2
                    .mapOf(int.class, String.class)
                    .pick("B28:F29", "G31")
                    .go();
            Assert.assertEquals(value, Jackson.parseTo(new TypeReference<Map<Integer, String>>() {
            }, "/json/e2/map/range/_6.json"));
        });
    }

    public void _7() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            Map<Integer, String> value = e2
                    .mapOf(int.class, String.class)
                    .pick("B28:F29", "G31")
                    .until(field -> field.toInt() == 8)
                    .go();
            Assert.assertEquals(value, Jackson.parseTo(new TypeReference<Map<Integer, String>>() {
            }, "/json/e2/map/range/_7.json"));
        });
    }

    public void _8() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            Map<Integer, String> value = e2
                    .mapOf(int.class, String.class)
                    .pick("B28:F29", "G31")
                    .until(field -> field.toInt() == 8)
                    .map(field -> field.toType(int.class) * 2)
                    .go();
            Assert.assertEquals(value, Jackson.parseTo(new TypeReference<Map<Integer, String>>() {
            }, "/json/e2/map/range/_8.json"));
        });
    }

    public void _9() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            Map<Integer, String> value = e2
                    .mapOf(int.class, String.class)
                    .pick("B28:F29", "G31")
                    .until(field -> field.toInt() == 8)
                    .map(field -> field.toType(int.class) * 2)
                    .mapValue(field -> field.toText() + "|")
                    .go();
            Assert.assertEquals(value, Jackson.parseTo(new TypeReference<Map<Integer, String>>() {
            }, "/json/e2/map/range/_9.json"));
        });
    }

    public void _10() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            Map<Integer, String> value = e2
                    .mapOf(int.class, String.class)
                    .pick("B28:F40", "G31")
                    .until(BindField::isBlank)
                    .map(field -> field.toType(int.class) * 2)
                    .mapValue(field -> field.toText() + "|")
                    .go();
            Assert.assertEquals(value, Jackson.parseTo(new TypeReference<Map<Integer, String>>() {
            }, "/json/e2/map/range/_10.json"));
        });
    }

    public void _11() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            Map<Integer, String> value = e2
                    .mapOf(int.class, String.class)
                    .pick("B28:F40", "G31")
                    .filter(field -> field.toInt() % 2 == 0)
                    .until(BindField::isBlank)
                    .map(field -> field.toType(int.class) * 2)
                    .mapValue(field -> field.toText() + "|")
                    .go();
            Assert.assertEquals(value, Jackson.parseTo(new TypeReference<Map<Integer, String>>() {
            }, "/json/e2/map/range/_11.json"));
        });
    }

    public void _12() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            Map<Long, String> value = e2
                    .mapOf(long.class, String.class)
                    .pick("B28:F40", "G31")
                    .filter(field -> field.toLong() % 2 == 0)
                    .until(BindField::isBlank)
                    .map(field -> field.toType(long.class) * 2)
                    .mapValue(field -> field.toText() + "|")
                    .go();
            Assert.assertEquals(value, Jackson.parseTo(new TypeReference<Map<Long, String>>() {
            }, "/json/e2/map/range/_11.json"));
        });
    }

    public void _13() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            Map<String, String> value = e2
                    .mapOf(String.class, String.class)
                    .pick("B28:F40", "G31")
                    .filter(field -> field.toLong() % 2 == 0)
                    .until(BindField::isBlank)
                    .map(field -> field.toType(long.class) * 2 + "")
                    .mapValue(field -> field.toText() + "|")
                    .go();
            Assert.assertEquals(value, Jackson.parseTo(new TypeReference<Map<String, String>>() {
            }, "/json/e2/map/range/_11.json"));
        });
    }

}
