package com.sombrainc.excelorm.e2.unit;

import com.sombrainc.excelorm.e2.impl.EReaderContext;
import com.sombrainc.excelorm.e2.impl.MiddleExecutor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.sombrainc.excelorm.utils.ExcelUtils.obtainRange;

@Test
public class MiddleExecutorTest {

    @DataProvider
    public static Object[][] makeTheSameLengthAndDirectionData() {
        return new Object[][]{
                {obtainRange("c1:c4"), obtainRange("b1"), obtainRange("b1:b4")},
                {obtainRange("a1:b4"), obtainRange("b1"), obtainRange("b1:c4")},
                {obtainRange("a1:a4"), obtainRange("b1"), obtainRange("b1:b4")},
        };
    }

    @Test(dataProvider = "makeTheSameLengthAndDirectionData")
    public void makeTheSameLengthAndDirectionTest(
            CellRangeAddress parent, CellRangeAddress child, CellRangeAddress expected) {
        final CellRangeAddress actual = MiddleExecutorWrapper.makeTheSameLengthAndDirection(parent, child);
        Assert.assertEquals(actual, expected);
    }

    public static class MiddleExecutorWrapper extends MiddleExecutor<String> {
        public MiddleExecutorWrapper(EReaderContext context) {
            super(context);
        }

        @Override
        public String execute() {
            return null;
        }

        protected static CellRangeAddress makeTheSameLengthAndDirection(CellRangeAddress parent, CellRangeAddress singleCell) {
            return MiddleExecutor.makeTheSameLengthAndDirection(parent, singleCell);
        }
    }

}
