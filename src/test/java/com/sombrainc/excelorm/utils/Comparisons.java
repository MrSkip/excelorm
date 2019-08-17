package com.sombrainc.excelorm.utils;

import org.testng.Assert;

import java.util.*;

public class Comparisons {

    private Comparisons() {}

    public static <T> void compareLists(Collection<T> actual, Collection<T> expected) {
        Assert.assertEquals(actual.size(), expected.size(), "Sizes of the lists are different");
        for (T eItem : expected) {
            if (actual.stream().noneMatch(eItem::equals)) {
                Assert.fail(String.format("Expected %s does not exist", eItem));
            }
        }
    }

    @SuppressWarnings("all")
    public static<T, T1> void compareMaps(Map<T, T1> actual, Map<T, T1> expected) {
        Assert.assertEquals(actual.size(), expected.size(), "Sizes of the maps are different");
        for (Map.Entry<T, T1> eEntry : expected.entrySet()) {
            Assert.assertTrue(actual.containsKey(eEntry.getKey()), String.format("Map does not contain key %s", eEntry.getKey()));
            T1 actualValue = actual.get(eEntry.getKey());
            if (eEntry.getValue() instanceof ArrayList) {
                compareLists((List) actualValue, (List) eEntry.getValue());
            } else {
                Assert.assertTrue(Objects.equals(actualValue, eEntry.getValue()),
                        String.format("Map contains different objects for the same key (%s) (expected) %s / %s", eEntry.getKey(), eEntry.getValue(), actualValue));
            }
        }
    }
}
