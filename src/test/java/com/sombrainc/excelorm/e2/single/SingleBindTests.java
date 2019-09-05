package com.sombrainc.excelorm.e2.single;

import com.sombrainc.excelorm.e2.dto.UserDTO;
import com.sombrainc.excelorm.e2.impl.Bind;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.sombrainc.excelorm.utils.ModelReader.executeForE2;

@Test
public class SingleBindTests {
    private static final String DEFAULT_SHEET = "e2Single";

    public void string() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            UserDTO value = e2.single(UserDTO.class)
                    .binds(new Bind("name", "A4")).go();
            Assert.assertEquals(new UserDTO().setName("name"), value);
        });
    }

    public void twoStrings() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            UserDTO value = e2.single(UserDTO.class)
                    .binds(
                            new Bind("name", "A4"),
                            new Bind("intAsStr", "A1")
                    ).go();
            Assert.assertEquals(new UserDTO().setName("name").setIntAsStr("1"), value);
        });
    }

    public void stringsOfList() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            UserDTO value = e2.single(UserDTO.class)
                    .binds(
                            new Bind("name", "A4"),
                            new Bind("intAsStr", "A1"),
                            new Bind("listOfIntAsStr", "B8:B13")
                    ).go();
            Assert.assertEquals(
                    new UserDTO()
                            .setName("name")
                            .setIntAsStr("1")
                            .setListOfIntAsStr(
                                    IntStream.rangeClosed(1, 6)
                                            .mapToObj(v -> v + "").collect(Collectors.toList()))
                    , value);
        });
    }

    public void stringsOfSet() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            UserDTO value = e2.single(UserDTO.class)
                    .binds(
                            new Bind("setOfIntAsStr", "I8:I16")
                    ).go();
            Assert.assertEquals(
                    new UserDTO()
                            .setSetOfIntAsStr(
                                    IntStream.rangeClosed(1, 7)
                                            .mapToObj(v -> v + "").collect(Collectors.toSet()))
                    , value);
        });
    }

    public void stringsOfSetUntilSpecialSymbol() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            UserDTO value = e2.single(UserDTO.class)
                    .binds(
                            new Bind("setOfIntAsStr", "I8:I19")
                                    .until(field -> !field.toText().contains("*"))
                    ).go();
            Assert.assertEquals(
                    new UserDTO()
                            .setSetOfIntAsStr(
                                    IntStream.rangeClosed(1, 8)
                                            .mapToObj(v -> v + "").collect(Collectors.toSet()))
                    , value);
        });
    }

    public void stringsOfSetUntilSpecialSymbol2() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            UserDTO value = e2.single(UserDTO.class)
                    .binds(
                            new Bind("setOfIntAsStr", "I8:I19")
                                    .until(field -> !field.toText().contains("*")),
                            new Bind("listOfInt", "K13:O13")
                                    .until(field -> !field.toText().contains("&"))
                    ).go();
            Assert.assertEquals(
                    new UserDTO()
                            .setSetOfIntAsStr(
                                    IntStream.rangeClosed(1, 8)
                                            .mapToObj(v -> v + "").collect(Collectors.toSet()))
                            .setListOfInt(
                                    IntStream.rangeClosed(1, 3)
                                            .boxed().collect(Collectors.toList()))
                    , value);
        });
    }

    public void stringsOfSetUntilLikeSpecialSymbol3() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            UserDTO value = e2.single(UserDTO.class)
                    .binds(
                            new Bind("setOfIntAsStr", "I8:I19")
                                    .until(field -> !field.toText().contains("*")),
                            new Bind("listOfInt", "K13:O13")
                                    .until(field -> !field.toText().contains("&"))
                                    .filter(field -> field.toInt() <= 2)
                    ).go();
            Assert.assertEquals(
                    new UserDTO()
                            .setSetOfIntAsStr(
                                    IntStream.rangeClosed(1, 8)
                                            .mapToObj(v -> v + "").collect(Collectors.toSet()))
                            .setListOfInt(
                                    IntStream.rangeClosed(1, 2)
                                            .boxed().collect(Collectors.toList()))
                    , value);
        });
    }

    public void stringsOfSetUntilLikeSpecialSymbol4() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            UserDTO value = e2.single(UserDTO.class)
                    .binds(
                            new Bind("setOfIntAsStr", "I8:I19")
                                    .map(field -> field.toInt() * 10 + "")
                                    .until(field -> !field.toText().contains("*")),
                            new Bind("listOfInt", "K13:O13")
                                    .until(field -> !field.toText().contains("&"))
                                    .filter(field -> field.toInt() <= 2)
                    ).go();
            Assert.assertEquals(
                    new UserDTO()
                            .setSetOfIntAsStr(
                                    IntStream.rangeClosed(1, 8)
                                            .mapToObj(v -> (v*10) + "").collect(Collectors.toSet()))
                            .setListOfInt(
                                    IntStream.rangeClosed(1, 2)
                                            .boxed().collect(Collectors.toList()))
                    , value);
        });
    }

}
