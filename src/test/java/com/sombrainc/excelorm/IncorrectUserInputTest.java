package com.sombrainc.excelorm;

import com.sombrainc.excelorm.exception.IncorrectRangeException;
import com.sombrainc.excelorm.exception.TypeIsNotSupportedException;
import com.sombrainc.excelorm.models.badinput.*;
import com.sombrainc.excelorm.utils.ModelReader;
import org.testng.annotations.Test;

@Test
public class IncorrectUserInputTest {

    @Test(expectedExceptions = IncorrectRangeException.class)
    public void expectedErrorForSingleTypeTest() {
        ModelReader.getModel(IncorrectModel1.class);
    }

    @Test(expectedExceptions = IncorrectRangeException.class)
    public void expectedErrorForSingleTypeIncorrectRangeTest() {
        ModelReader.getModel(IncorrectModel2.class);
    }

    @Test(expectedExceptions = IncorrectRangeException.class)
    public void expectedErrorForIncorrectRangeSingleTypeTest() {
        ModelReader.getModel(IncorrectModel3.class);
    }

    @Test(expectedExceptions = TypeIsNotSupportedException.class)
    public void expectedErrorWhenUseIncorrectAnnotationForMapTest() {
        ModelReader.getModel(IncorrectModel4.class);
    }

    @Test(expectedExceptions = TypeIsNotSupportedException.class)
    public void expectedErrorWhenUseIncorrectAnnotationTest() {
        ModelReader.getModel(IncorrectModel5.class);
    }

    @Test(expectedExceptions = IncorrectRangeException.class)
    public void expectedErrorForMapTest() {
        ModelReader.getModel(IncorrectModel6.class);
    }

}
