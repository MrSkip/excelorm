package com.sombrainc.excelorm;

import com.sombrainc.excelorm.models.simple.Model1;
import com.sombrainc.excelorm.models.simple.Model2;
import com.sombrainc.excelorm.models.simple.NestedClassModel;
import com.sombrainc.excelorm.utils.ModelReader;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;

@Test
public class BasicTypesTest {

    public void testPureNumbersScenario1() {
        Model1 model = ModelReader.getModel(Model1.class);
        Assert.assertEquals(model.getA1(), 1);
        Assert.assertEquals(model.getB1(), "name");
        Assert.assertEquals(model.getC1(), 1.3);
        Assert.assertEquals(model.getC1Float(), (float) 1.3);
        Assert.assertEquals(model.getC1FloatPrimitive(), (float) 1.3);
        Assert.assertEquals(model.getC1BigDecimal(), BigDecimal.valueOf(1.3));
        Assert.assertEquals(model.getA1BigDecimal(), BigDecimal.valueOf(1.0));
        Assert.assertTrue(model.isD1());
        Assert.assertTrue(model.getD1BoolObject());
    }

    public void nestedClassTest() {
        NestedClassModel nestedClassModel = ModelReader.getModel(NestedClassModel.class);
        Assert.assertNotNull(nestedClassModel);
        NestedClassModel.NestedClass model = nestedClassModel.getNestedClass();
        Assert.assertEquals(model.getA1(), 1);
        Assert.assertEquals(model.getB1(), "name");
        Assert.assertEquals(model.getC1(), 1.3);
        Assert.assertEquals(model.getC1Float(), (float) 1.3);
        Assert.assertEquals(model.getC1FloatPrimitive(), (float) 1.3);
        Assert.assertEquals(model.getC1BigDecimal(), BigDecimal.valueOf(1.3));
        Assert.assertEquals(model.getA1BigDecimal(), BigDecimal.valueOf(1.0));
        Assert.assertTrue(model.isD1());
        Assert.assertTrue(model.getD1BoolObject());
    }

    public void testPureNumbersScenario2() {
        Model2 model = ModelReader.getModel(Model2.class);
        Assert.assertEquals(model.getA1(), 2);
        Assert.assertEquals(model.getB1(), "name2");
        Assert.assertEquals(model.getC1(), 21.3);
        Assert.assertFalse(model.isD1());
    }

}
