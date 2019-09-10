package com.sombrainc.excelorm.e2.single;

import com.sombrainc.excelorm.e2.dto.UserDTO;
import com.sombrainc.excelorm.e2.impl.Bind;
import com.sombrainc.excelorm.e2.impl.BindField;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
                    .map(cell -> cell.toText() + 1)
                    .pick("A4:B7")
                    .go();
            Assert.assertEquals("name1", value);
        });
    }

    public void stringRangeWithMapperAsNull() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            String value = e2
                    .single(String.class)
                    .map(cell -> null)
                    .pick("A4:B7")
                    .go();
            Assert.assertNull(value);
        });
    }

    public void doubleTest() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            double value = e2
                    .single(double.class)
                    .map(cell -> cell.cell().getNumericCellValue())
                    .pick("A2")
                    .go();
            Assert.assertEquals(value, 2.2);
        });
    }

    public void doubleTest2() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            double value = e2
                    .single(double.class)
                    .map(BindField::toDouble)
                    .pick("A2")
                    .go();
            Assert.assertEquals(value, 2.2);
        });
    }

    public void longTest() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            long value = e2
                    .single(long.class)
                    .map(field -> Long.parseLong(field.toText().split("Test")[1]))
                    .pick("E20")
                    .go();
            Assert.assertEquals(value, 3);
        });
    }

    public void stringEmptyTest() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            String value = e2
                    .single(String.class)
                    .pick("AZ1")
                    .go();
            Assert.assertEquals(value, "");
        });
    }

}
