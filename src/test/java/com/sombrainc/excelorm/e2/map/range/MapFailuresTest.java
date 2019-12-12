package com.sombrainc.excelorm.e2.map.range;

import com.sombrainc.excelorm.e2.impl.BindField;
import com.sombrainc.excelorm.exception.IncorrectRangeException;
import com.sombrainc.excelorm.exception.POIRuntimeException;
import com.sombrainc.excelorm.exception.TypeIsNotSupportedException;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Map;

import static com.sombrainc.excelorm.utils.ModelReader.executeForE2;

public class MapFailuresTest {
    private static final String DEFAULT_MAP_SHEET = "e2Map";

    @Test(expectedExceptions = IncorrectRangeException.class)
    public void range_1() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            e2
                    .mapOf(String.class, String.class)
                    .pick(null, "H37:J39")
                    .go();
        });
    }

    @Test(expectedExceptions = POIRuntimeException.class)
    public void _1_1() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            e2
                    .mapOf(String.class, LocalDate.class)
                    .pick("B3:B7", "D9")
                    .go();
        });
    }

    @Test(expectedExceptions = IncorrectRangeException.class)
    public void range_2() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            e2
                    .mapOf(String.class, String.class)
                    .pick("", "H37:J39")
                    .go();
        });
    }

    @Test(expectedExceptions = IncorrectRangeException.class)
    public void range_3() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            e2
                    .mapOf(String.class, String.class)
                    .pick("H37:J39", null)
                    .go();
        });
    }

    @Test(expectedExceptions = IncorrectRangeException.class)
    public void range_4() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            e2
                    .mapOf(String.class, String.class)
                    .pick("H37:J39", "")
                    .go();
        });
    }

    @Test(expectedExceptions = POIRuntimeException.class)
    public void mapOf_1() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            e2
                    .mapOf(null, String.class)
                    .pick("H37:J39", "H37:J39")
                    .go();
        });
    }

    @Test(expectedExceptions = POIRuntimeException.class)
    public void mapOf_2() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            e2
                    .mapOf(int.class, null)
                    .pick("H37:J39", "H37:J39")
                    .go();
        });
    }

    @Test(expectedExceptions = POIRuntimeException.class)
    public void incorrect_1() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            e2
                    .mapOf(int.class, int.class)
                    .pick("D3", "D4")
                    .go();
        });
    }

    @Test(expectedExceptions = IncorrectRangeException.class)
    public void incorrect_2() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            final Map<Integer, String> go = e2
                    .mapOf(int.class, String.class)
                    .pick("B3:B7", "B3:B8")
                    .go();
        });
    }

    @Test(expectedExceptions = IncorrectRangeException.class)
    public void incorrect_3() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            e2
                    .mapOf(int.class, String.class)
                    .pick("B3:B9", "B3:B8")
                    .go();
        });
    }

    @Test(expectedExceptions = POIRuntimeException.class)
    public void incorrect_5() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            e2
                    .mapOf(int.class, String.class)
                    .pick("B3:B22", "B3:B22")
                    .map(BindField::toInt)
                    .go();
        });
    }

    @Test(expectedExceptions = POIRuntimeException.class)
    public void incorrect_6() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            final Map<Integer, String> go = e2
                    .mapOf(int.class, String.class)
                    .pick("B3:B5", "F3:G3")
                    .go();
        });
    }

}
