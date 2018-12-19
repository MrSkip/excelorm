package com.sombrainc.excelorm;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class Test1 {

    public void testPureNumbers() {
        Model1 model = ModelContainer.getModel(Model1.class);
        Assert.assertEquals(model.getA1(), 1);
        Assert.assertEquals(model.getB1(), "name");
        Assert.assertEquals(model.getC1(), 1.3);
        Assert.assertTrue(model.isD1());
    }

}
