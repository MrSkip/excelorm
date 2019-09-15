package com.sombrainc.excelorm.e2.map.list;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sombrainc.excelorm.exception.HasNotImplementedYetException;
import com.sombrainc.excelorm.exception.IncorrectRangeException;
import com.sombrainc.excelorm.utils.Comparisons;
import com.sombrainc.excelorm.utils.Jackson;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import static com.sombrainc.excelorm.utils.ModelReader.executeForE2;

@Test
public class MapListFailuresTest {
    private static final String DEFAULT_MAP_SHEET = "mapList";

    @Test(expectedExceptions = IncorrectRangeException.class)
    public void _1() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            Map<String, List<String>> value = e2
                    .mapOfList(String.class, String.class)
                    .pick("B4", "D4:F4")
                    .go();
            Comparisons.compareMaps(value, Jackson.parseTo(new TypeReference<Map<String, List<String>>>() {
            }, "/json/e2/map/list/_5.json"));
        });
    }

    @Test(expectedExceptions = IncorrectRangeException.class)
    public void _2() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            Map<String, List<String>> value = e2
                    .mapOfList(String.class, String.class)
                    .pick("D4:F4", "C1")
                    .go();
            Comparisons.compareMaps(value, Jackson.parseTo(new TypeReference<Map<String, List<String>>>() {
            }, "/json/e2/map/list/_5.json"));
        });
    }

    @Test(expectedExceptions = HasNotImplementedYetException.class)
    public void _3() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            Map<String, List<List>> value = e2
                    .mapOfList(String.class, List.class)
                    .pick("D4:F4", "D4:F4")
                    .go();
            Comparisons.compareMaps(value, Jackson.parseTo(new TypeReference<Map<String, List<List>>>() {
            }, "/json/e2/map/list/_5.json"));
        });
    }

    @Test(expectedExceptions = IncorrectRangeException.class)
    public void _4() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            Map<String, List<String>> value = e2
                    .mapOfList(String.class, String.class)
                    .pick("D4:F4", "D4:F4")
                    .go();
            Comparisons.compareMaps(value, Jackson.parseTo(new TypeReference<Map<String, List<String>>>() {
            }, "/json/e2/map/list/_5.json"));
        });
    }

}
