package com.sombrainc.excelorm;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.stream.Stream;

@Test
public class Test1 {

    public void testPureNumbers1() {
        Model1 model = ModelContainer.getModel(Model1.class);
        Assert.assertEquals(model.getA1(), 1);
        Assert.assertEquals(model.getB1(), "name");
        Assert.assertEquals(model.getC1(), 1.3);
        Assert.assertTrue(model.isD1());
    }

    public void testPureNumbers2() {
        Model2 model = ModelContainer.getModel(Model2.class);
        Assert.assertEquals(model.getA1(), 2);
        Assert.assertEquals(model.getB1(), "name2");
        Assert.assertEquals(model.getC1(), 21.3);
        Assert.assertFalse(model.isD1());
    }

    public void testPureNumbers3() {
        Model3 model = ModelContainer.getModel(Model3.class);
        Assert.assertTrue(model.getList() != null && !model.getList().isEmpty());
        Assert.assertEquals(model.getList().get(0), "n1");
    }

    public void testPureNumbers4() {
        Model4 model = ModelContainer.getModel(Model4.class);
        Assert.assertTrue(model.getList() != null && model.getList().size() == 12);
        Stream
                .iterate(1, n -> n + 1)
                .limit(12).forEach(value -> {
            String expectedValue = "n" + value;
            Assert.assertTrue(model.getList().contains(expectedValue), expectedValue);
        });
    }

    public void testPureNumbers5() {
        Model5 model = ModelContainer.getModel(Model5.class);
        Assert.assertTrue(model.getList() != null && model.getList().size() == 4);
        Stream
                .iterate(1, n -> n + 1)
                .limit(4).forEach(value -> {
            String expectedValue = "n" + value;
            Assert.assertTrue(model.getList().contains(expectedValue), expectedValue);
        });
    }

    public void testPureNumbers6() {
        Model6 model = ModelContainer.getModel(Model6.class);
        Assert.assertTrue(model.getList() != null && model.getList().size() == 3);
        Stream
                .iterate(1, n -> n + 4)
                .limit(3).forEach(value -> {
            String expectedValue = "n" + value;
            Assert.assertTrue(model.getList().contains(expectedValue), expectedValue);
        });
    }

}
