package com.sombrainc.excelorm.e2;

import com.sombrainc.excelorm.e2.single.SingleTest;
import com.sombrainc.excelorm.utils.ModelReader;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;

@Test
public class EReaderTest {

    private static void quickAssertion(EReader eReader) {
        final String value = eReader
                .single(String.class)
                .pick("A1")
                .go();
        Assert.assertEquals("1", value);
    }

    public void eReaderWithPathAndSheetNameTest() {
        quickAssertion(new EReader(EReaderTest.class.getResource(ModelReader.PATH).getPath(), SingleTest.DEFAULT_SHEET));
    }

    public void eReaderWithPathTest() {
        quickAssertion(new EReader(EReaderTest.class.getResource(ModelReader.PATH).getPath()));
    }

    public void eReaderWithInputStreamTest() {
        quickAssertion(new EReader(EReaderTest.class.getResourceAsStream(ModelReader.PATH)));
    }

    public void eReaderWithInputStreamAndSheetNameTest() {
        final EReader eReader = new EReader(EReaderTest.class.getResourceAsStream(ModelReader.PATH), SingleTest.DEFAULT_SHEET);
        quickAssertion(eReader);
    }

    public void eReaderWithFileAndSheetName() {
        quickAssertion(new EReader(new File(EReaderTest.class.getResource(ModelReader.PATH).getPath()), SingleTest.DEFAULT_SHEET));
    }

    public void eReaderWithFile() {
        quickAssertion(new EReader(new File(EReaderTest.class.getResource(ModelReader.PATH).getPath())));
    }

}
