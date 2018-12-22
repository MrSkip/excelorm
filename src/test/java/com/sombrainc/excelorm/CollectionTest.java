package com.sombrainc.excelorm;

import com.sombrainc.excelorm.models.*;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Test
public class CollectionTest {
    private ModelCollection model;

    @BeforeClass
    public void prepareModelObject() {
        model = ModelContainer.getModel(ModelCollection.class);
    }

    public void testListStringOneCell() {
        Model3 model = ModelContainer.getModel(Model3.class);
        Assert.assertTrue(model.getList() != null && model.getList().size() == 1);
        Assert.assertEquals(model.getList().get(0), "n1");
    }

    public void testListStringRange() {
        Model4 model = ModelContainer.getModel(Model4.class);
        Assert.assertTrue(model.getList() != null && model.getList().size() == 12);
        Stream
                .iterate(1, n -> n + 1)
                .limit(12).forEach(value -> {
            String expectedValue = "n" + value;
            Assert.assertTrue(model.getList().contains(expectedValue), expectedValue);
        });
    }

    public void testListStringColumnUntilNull() {
        Model5 model = ModelContainer.getModel(Model5.class);
        Assert.assertTrue(model.getList() != null && model.getList().size() == 4);
        Stream
                .iterate(1, n -> n + 1)
                .limit(4).forEach(value -> {
            String expectedValue = "n" + value;
            Assert.assertTrue(model.getList().contains(expectedValue), expectedValue);
        });
    }

    public void testListStringColumnRowNull() {
        Model6 model = ModelContainer.getModel(Model6.class);
        Assert.assertTrue(model.getList() != null && model.getList().size() == 3);
        Stream
                .iterate(1, n -> n + 4)
                .limit(3).forEach(value -> {
            String expectedValue = "n" + value;
            Assert.assertTrue(model.getList().contains(expectedValue), expectedValue);
        });
    }

    public void testListIntegerFixed() {
        List<Integer> list = model.getIntsFixed();
        Assert.assertTrue(list != null && list.size() == 36);
        Stream
                .iterate(1, n -> n + 1)
                .limit(36).forEach(value -> {
            Assert.assertTrue(list.contains(value), String.format("Expected value is %s", value));
        });
    }

    public void testListIntegerRowUntilNull() {
        List<Integer> list = model.getIntsRowUntilNull();
        Assert.assertTrue(list != null && list.size() == 4);
        Stream
                .iterate(1, n -> n + 9)
                .limit(4).forEach(value -> {
            Assert.assertTrue(list.contains(value), String.format("Expected value is %s", value));
        });
    }

    public void testListIntegerColomnUntilNull() {
        List<Integer> list = model.getIntsColUntilNull();
        Assert.assertTrue(list != null && list.size() == 9);
        Stream
                .iterate(1, n -> n + 1)
                .limit(9).forEach(value -> {
            Assert.assertTrue(list.contains(value), String.format("Expected value is %s", value));
        });
    }

    public void testListIntegerColumnUntilNullStep2() {
        List<Integer> list = model.getIntsColUntilNullStep2();
        Assert.assertTrue(list != null && list.size() == 5);
        Stream
                .iterate(1, n -> n + 2)
                .limit(5).forEach(value -> {
            Assert.assertTrue(list.contains(value), String.format("Expected value is %s", value));
        });
    }

    public void testListDecimalsRange() {
        List<BigDecimal> list = model.getDecimals();
        Assert.assertTrue(list != null && list.size() == 27);
        for (double i = 1.1; i <= 3.7; i += 0.1) {
            Assert.assertTrue(list.contains(BigDecimal.valueOf(i).setScale(1, RoundingMode.CEILING)),
                    String.format("Expected value is %s", i));
        }
    }

    public void testListBoolRange() {
        List<Boolean> list = model.getBooleans();
        Assert.assertTrue(list != null && list.size() == 27);
        for (int i = 0; i < 27; i++) {
            Assert.assertEquals((boolean) list.get(i), (i + 2) % 2 == 0);
        }
    }

    public void testSetDecimalRange() {
        Set<BigDecimal> set = model.getDecimalsSet();
        Assert.assertTrue(set != null && set.size() == 27);
        for (double i = 1.1; i <= 3.7; i += 0.1) {
            Assert.assertTrue(set.contains(BigDecimal.valueOf(i).setScale(1, RoundingMode.CEILING)),
                    String.format("Expected value is %s", i));
        }
    }

    public void testCollectionDecimalRange() {
        Collection<BigDecimal> collection = model.getDecimalsCollection();
        Assert.assertTrue(collection != null && collection.size() == 27);
        for (double i = 1.1; i <= 3.7; i += 0.1) {
            Assert.assertTrue(collection.contains(BigDecimal.valueOf(i).setScale(1, RoundingMode.CEILING)),
                    String.format("Expected value is %s", i));
        }
    }

}
