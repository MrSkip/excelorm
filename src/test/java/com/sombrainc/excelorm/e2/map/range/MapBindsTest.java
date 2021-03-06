package com.sombrainc.excelorm.e2.map.range;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sombrainc.excelorm.e2.impl.Bind;
import com.sombrainc.excelorm.e2.impl.BindField;
import com.sombrainc.excelorm.e2.map.range.dto.User;
import com.sombrainc.excelorm.utils.Jackson;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Map;

import static com.sombrainc.excelorm.e2.utils.EFilters.isBlank;
import static com.sombrainc.excelorm.utils.ModelReader.executeForE2;

@Test
public class MapBindsTest {
    private static final String DEFAULT_MAP_SHEET = "e2MapBinds";

    public void _1() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            Map<String, User> value = e2
                    .mapOf(String.class, User.class)
                    .pick("B3:B7", "D9")
                    .binds(
                            new Bind("symbol", "D9"),
                            new Bind("integer", "B3")
                    )
                    .go();
            Assert.assertEquals(value, Jackson.parseTo(new TypeReference<Map<String, User>>() {
            }, "/json/e2/map/range/bind/_1.json"));
        });
    }

    public void _1_1() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            Map<LocalDate, User> value = e2
                    .mapOf(LocalDate.class, User.class)
                    .pick("a21:a22", "D9")
                    .binds(
                            new Bind("symbol", "D9"),
                            new Bind("integer", "B3")
                    )
                    .map(field -> LocalDate.parse(field.toText()))
                    .go();
            Assert.assertEquals(value, Jackson.parseTo(new TypeReference<Map<LocalDate, User>>() {
            }, "/json/e2/map/range/bind/_1_1.json"));
        });
    }

    public void _2() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            Map<String, User> value = e2
                    .mapOf(String.class, User.class)
                    .pick("B3:B7", "D9")
                    .binds(
                            new Bind("symbol", "D9"),
                            new Bind("integer", "B3"),
                            new Bind("integers", "G5:I5")
                    )
                    .go();
            Assert.assertEquals(value, Jackson.parseTo(new TypeReference<Map<String, User>>() {
            }, "/json/e2/map/range/bind/_2.json"));
        });
    }

    public void _3() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            Map<String, User> value = e2
                    .mapOf(String.class, User.class)
                    .pick("B3:B7", "D9")
                    .binds(
                            new Bind("symbol", "D9"),
                            new Bind("integer", "B3"),
                            new Bind("integers", "G11:I11")
                                    .filter(BindField::isNotBlank)
                                    .map(field -> field.toInt() + 1)
                    )
                    .go();
            Assert.assertEquals(value, Jackson.parseTo(new TypeReference<Map<String, User>>() {
            }, "/json/e2/map/range/bind/_3.json"));
        });
    }

    public void _4() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            Map<String, User> value = e2
                    .mapOf(String.class, User.class)
                    .pick("B3:B7", "D9")
                    .binds(
                            new Bind("symbol", "D9"),
                            new Bind("integer", "B3"),
                            new Bind("integers", "G11:I11")
                                    .until(isBlank())
                                    .filter(BindField::isNotBlank)
                                    .map(field -> field.toInt() + 1)
                    )
                    .go();
            Assert.assertEquals(value, Jackson.parseTo(new TypeReference<Map<String, User>>() {
            }, "/json/e2/map/range/bind/_4.json"));
        });
    }

    public void _5() {
        executeForE2(DEFAULT_MAP_SHEET, e2 -> {
            Map<String, User> value = e2
                    .mapOf(String.class, User.class)
                    .pick("B3:B7", "D9")
                    .binds(
                            new Bind("symbol", "D9:D20"),
                            new Bind("integer", "B3"),
                            new Bind("integers", "G11:I11")
                                    .until(isBlank())
                                    .filter(BindField::isNotBlank)
                                    .map(field -> field.toInt() + 1)
                    )
                    .go();
            Assert.assertEquals(value, Jackson.parseTo(new TypeReference<Map<String, User>>() {
            }, "/json/e2/map/range/bind/_4.json"));
        });
    }

}
