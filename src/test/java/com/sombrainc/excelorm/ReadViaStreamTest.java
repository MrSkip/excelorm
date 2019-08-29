package com.sombrainc.excelorm;

import com.sombrainc.excelorm.exception.POIRuntimeException;
import com.sombrainc.excelorm.models.simple.Model1;
import com.sombrainc.excelorm.utils.ModelReader;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;

@Test
public class ReadViaStreamTest {

    public void readUsingInputStreamTest() {
        Model1 model = ModelReader.getModel(
                ReadViaStreamTest.class.getResourceAsStream(ModelReader.PATH), Model1.class, "position");
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

    @Test(expectedExceptions = POIRuntimeException.class)
    public void readUsingInputStreamExceptionTest() {
        ModelReader.getModel(null, Model1.class, "position");
    }

    @Test(expectedExceptions = POIRuntimeException.class)
    public void readUsingInputStreamSheetInNullTest() {
        ModelReader.getModel(ReadViaStreamTest.class.getResourceAsStream(ModelReader.PATH), Model1.class, null);
    }

    @Test(expectedExceptions = POIRuntimeException.class)
    public void readUsingInputStreamSheetInEmptyTest() {
        ModelReader.getModel(ReadViaStreamTest.class.getResourceAsStream(ModelReader.PATH), Model1.class, " ");
    }

    @Test(expectedExceptions = POIRuntimeException.class)
    public void readUsingInputStreamBadSheetNameTest() {
        ModelReader.getModel(ReadViaStreamTest.class.getResourceAsStream(ModelReader.PATH), Model1.class, "fs234f&");
    }
}
