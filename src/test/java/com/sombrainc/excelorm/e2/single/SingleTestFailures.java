package com.sombrainc.excelorm.e2.single;

import com.sombrainc.excelorm.e2.dto.UserDTO;
import com.sombrainc.excelorm.e2.impl.Bind;
import com.sombrainc.excelorm.exception.POIRuntimeException;
import com.sombrainc.excelorm.exception.TypeIsNotSupportedException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.sombrainc.excelorm.utils.ModelReader.executeForE2;

@Test
public class SingleTestFailures {
    private static final String DEFAULT_SHEET = "e2Single";

    @Test(expectedExceptions = POIRuntimeException.class)
    public void numberAsText() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            e2
                    .single(null)
                    .pick("A1")
                    .go();
        });
    }

    @Test(expectedExceptions = POIRuntimeException.class)
    public void doubleAsInt() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            int value = e2
                    .single(int.class)
                    .pick("A2")
                    .go();
            Assert.assertEquals(2, value);
        });
    }

    @Test(expectedExceptions = POIRuntimeException.class)
    public void textAsNumber() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            final int a1 = e2
                    .single(int.class)
                    .pick("C20")
                    .go();
            System.out.println(a1);
        });
    }

    @Test(expectedExceptions = POIRuntimeException.class)
    public void integer() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            int value = e2
                    .single(int.class)
                    .pick(null)
                    .go();
        });
    }

    @Test(expectedExceptions = POIRuntimeException.class)
    public void stringRange() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            String value = e2
                    .single(String.class)
                    .pick("A4:null")
                    .go();
            Assert.assertEquals("name", value);
        });
    }

    @Test(expectedExceptions = POIRuntimeException.class)
    public void singleCustomObjectString() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            UserDTO value = e2.single(UserDTO.class)
                    .binds(new Bind(null, "A4")).go();
            Assert.assertEquals(new UserDTO().setName("name"), value);
        });
    }

    @Test(expectedExceptions = POIRuntimeException.class)
    public void singleCustomObjectString2() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            e2.single(UserDTO.class)
                    .binds(
                            new Bind("name", null),
                            new Bind("intAsStr", "A1")
                    ).go();
        });
    }

    @Test(expectedExceptions = POIRuntimeException.class)
    public void singleCustomObjectStringList() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            e2.single(UserDTO.class)
                    .binds(
                            null,
                            new Bind("intAsStr", "A1"),
                            new Bind("listOfIntAsStr", "B8:B13")
                    ).go();
        });
    }

    @Test(expectedExceptions = TypeIsNotSupportedException.class)
    public void singleCustomObjectMapIsNotSupported() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            final UserDTO go = e2.single(UserDTO.class)
                    .binds(
                            new Bind("intAsStr", "A1"),
                            new Bind("map", "B8:B13")
                    ).go();
        });
    }

    @Test(expectedExceptions = POIRuntimeException.class)
    public void empty() {
        executeForE2(DEFAULT_SHEET, e2 -> e2
                .single(int.class)
                .pick("AX1")
                .go());
    }

    @Test(expectedExceptions = POIRuntimeException.class)
    public void customType_mappingIsMissing() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            LocalDate value = e2
                    .single(LocalDate.class)
                    .pick("AZ1")
                    .go();
        });
    }

}
