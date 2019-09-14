package com.sombrainc.excelorm.e2.map.range;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sombrainc.excelorm.e2.impl.Bind;
import com.sombrainc.excelorm.e2.impl.BindField;
import com.sombrainc.excelorm.e2.map.range.dto.User;
import com.sombrainc.excelorm.exception.FieldNotFoundException;
import com.sombrainc.excelorm.exception.IncorrectRangeException;
import com.sombrainc.excelorm.exception.POIRuntimeException;
import com.sombrainc.excelorm.exception.TypeIsNotSupportedException;
import com.sombrainc.excelorm.utils.Jackson;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

import static com.sombrainc.excelorm.utils.ModelReader.executeForE2;

public class MapFailureBindsTest {
    private static final String DEFAULT_MAP_SHEET = "e2Map";

    @Test(expectedExceptions = IncorrectRangeException.class)
    public void _1() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            Map<String, User> value = e2
                    .mapOf(String.class, User.class)
                    .pick("B3:B7", "D9")
                    .binds(
                            new Bind("symbol", ""),
                            new Bind("integer", "B3"),
                            new Bind("integer1", "B3")
                    )
                    .go();
            Assert.assertEquals(value, Jackson.parseTo(new TypeReference<Map<String, User>>() {
            }, "/json/e2/map/bind/_1.json"));
        });
    }

    @Test(expectedExceptions = POIRuntimeException.class)
    public void _2() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            Map<String, User> value = e2
                    .mapOf(String.class, User.class)
                    .pick("B3:B7", "D9")
                    .binds(
                            null,
                            new Bind("integer", "B3"),
                            new Bind("integer1", "B3")
                    )
                    .go();
            Assert.assertEquals(value, Jackson.parseTo(new TypeReference<Map<String, User>>() {
            }, "/json/e2/map/bind/_1.json"));
        });
    }

}
