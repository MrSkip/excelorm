package com.sombrainc.excelorm.e2.map;

import com.sombrainc.excelorm.e2.utils.EFilters;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

}
