package com.sombrainc.excelorm.e2;

import org.testng.Assert;
import org.testng.annotations.Test;

import static com.sombrainc.excelorm.utils.ModelReader.executeForE2;

@Test
public class SingleTest {
    private static final String DEFAULT_SHEET = "e2Single";

    public void numberAsText() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            String value = e2
                    .single(String.class)
                    .pick("A1")
                    .go();
            Assert.assertEquals("1", value);
        });
    }

    public void integer() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            int value = e2
                    .single(int.class)
                    .pick("A1")
                    .go();
            Assert.assertEquals(1, value);
        });
    }

    public void string() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            String value = e2
                    .single(String.class)
                    .pick("A4")
                    .go();
            Assert.assertEquals("name", value);
        });
    }

    public void booleanValue() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            boolean value = e2
                    .single(boolean.class)
                    .pick("A3")
                    .go();
            Assert.assertTrue(value);
        });
    }

    public void booleanAsText() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            String value = e2
                    .single(String.class)
                    .pick("A3")
                    .go();
            Assert.assertEquals("TRUE", value);
        });
    }

    public void stringRange() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            String value = e2
                    .single(String.class)
                    .pick("A4:B7")
                    .go();
            Assert.assertEquals("name", value);
        });
    }

    public void stringRangeWithMapper() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            String value = e2
                    .single(String.class)
                    .map(cell -> cell.getStringCellValue() + 1)
                    .pick("A4:B7")
                    .go();
            Assert.assertEquals("name1", value);
        });
    }

}
