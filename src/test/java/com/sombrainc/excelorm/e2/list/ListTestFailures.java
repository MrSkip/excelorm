package com.sombrainc.excelorm.e2.list;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sombrainc.excelorm.e2.dto.UserDTO;
import com.sombrainc.excelorm.e2.impl.Bind;
import com.sombrainc.excelorm.e2.utils.EFilters;
import com.sombrainc.excelorm.exception.POIRuntimeException;
import com.sombrainc.excelorm.exception.TypeIsNotSupportedException;
import com.sombrainc.excelorm.utils.Comparisons;
import com.sombrainc.excelorm.utils.Jackson;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.sombrainc.excelorm.e2.utils.EFilters.isNotBlank;
import static com.sombrainc.excelorm.utils.ModelReader.executeForE2;

@Test
public class ListTestFailures {
    private static final String DEFAULT_SHEET = "e2Single";

    @Test(expectedExceptions = TypeIsNotSupportedException.class)
    public void ofSet() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            e2
                    .listOf(Set.class)
                    .pick("B48:B50")
                    .go();
        });
    }

    @Test(expectedExceptions = TypeIsNotSupportedException.class)
    public void ofMap() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            e2
                    .listOf(Map.class)
                    .pick("B48:B50")
                    .go();
        });
    }

    @Test(expectedExceptions = POIRuntimeException.class)
    public void ofNull() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            e2
                    .listOf(null)
                    .pick("B48:B50")
                    .go();
        });
    }

    @Test(expectedExceptions = POIRuntimeException.class)
    public void pickIsNull() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            final List<String> go = e2
                    .listOf(String.class)
                    .pick(null)
                    .go();
        });
    }

    @Test(expectedExceptions = POIRuntimeException.class)
    public void pickIsIncorrect() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            e2
                    .listOf(String.class)
                    .pick("null")
                    .go();
        });
    }

    @Test(expectedExceptions = POIRuntimeException.class)
    public void pickIsIMissing() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            e2
                    .listOf(String.class)
                    .go();
        });
    }

    @Test(expectedExceptions = POIRuntimeException.class)
    public void customObjectFieldIsNull() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            List<UserDTO> value = e2
                    .listOf(UserDTO.class)
                    .binds(
                            new Bind(null, "M24:O24").filter(isNotBlank()),
                            new Bind("listOfIntAsStr", "M24:O24").filter(isNotBlank())
                    )
                    .pick("M24:M29")
                    .filter(EFilters::isNotBlank)
                    .go();
            Comparisons.compareLists(value, Jackson.parseTo(new TypeReference<List<UserDTO>>() {
            }, "/json/e2/list/customObjectWithTwoListInside.json"));
        });
    }

    @Test(expectedExceptions = POIRuntimeException.class)
    public void customObjectCellIsNull() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            List<UserDTO> value = e2
                    .listOf(UserDTO.class)
                    .binds(
                            new Bind("test", "M24:O24").filter(isNotBlank()),
                            new Bind("listOfIntAsStr", null).filter(isNotBlank())
                    )
                    .pick("M24:M29")
                    .filter(EFilters::isNotBlank)
                    .go();
            Comparisons.compareLists(value, Jackson.parseTo(new TypeReference<List<UserDTO>>() {
            }, "/json/e2/list/customObjectWithTwoListInside.json"));
        });
    }

    @Test(expectedExceptions = POIRuntimeException.class)
    public void customObjectCellIsIncorrect() {
        executeForE2(DEFAULT_SHEET, e2 -> {
            List<UserDTO> value = e2
                    .listOf(UserDTO.class)
                    .binds(
                            new Bind("test", "M24:O24").filter(isNotBlank()),
                            new Bind("listOfIntAsStr", "A-").filter(isNotBlank())
                    )
                    .pick("M24:M29")
                    .filter(EFilters::isNotBlank)
                    .go();
            Comparisons.compareLists(value, Jackson.parseTo(new TypeReference<List<UserDTO>>() {
            }, "/json/e2/list/customObjectWithTwoListInside.json"));
        });
    }
}
