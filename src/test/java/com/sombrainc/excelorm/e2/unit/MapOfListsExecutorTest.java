package com.sombrainc.excelorm.e2.unit;

import com.sombrainc.excelorm.e2.impl.map.list.MapOfLists;
import com.sombrainc.excelorm.e2.impl.map.list.MapOfListsExecutor;
import com.sombrainc.excelorm.exception.IncorrectRangeException;
import org.apache.poi.ss.util.CellRangeAddress;
import org.testng.annotations.Test;

import static com.sombrainc.excelorm.utils.ExcelUtils.*;

@Test
public class MapOfListsExecutorTest {

    @Test
    public void validate_1() {
        Wrapper.validate(obtainRange("C1"), obtainRange("C1"));
    }

    @Test
    public void validate_2() {
        Wrapper.validate(obtainRange("C1:C2"), obtainRange("C1:C2"));
    }

    @Test(expectedExceptions = IncorrectRangeException.class)
    public void validate_3() {
        Wrapper.validate(obtainRange("C1:C2"), obtainRange("C1"));
    }

    @Test(expectedExceptions = IncorrectRangeException.class)
    public void validate_4() {
        Wrapper.validate(obtainRange("C1:C2"), obtainRange("C1:C3"));
    }

    @Test
    public void validate_5() {
        Wrapper.validate(obtainRange("C1:C2"), obtainRange("B1:B2"));
    }

    @Test
    public void validate_6() {
        Wrapper.validate(obtainRange("C1:C2"), obtainRange("B1:C1"));
    }

    @Test
    public void validate_7() {
        Wrapper.validate(obtainRange("C1:D2"), obtainRange("E1:F2"));
    }

    @Test(expectedExceptions = IncorrectRangeException.class)
    public void validate_8() {
        Wrapper.validate(obtainRange("C1:C2"), obtainRange("E1:F2"));
    }

    @Test(expectedExceptions = IncorrectRangeException.class)
    public void validate_9() {
        Wrapper.validate(obtainRange("C1:D2"), obtainRange("E1:F4"));
    }

    public static class Wrapper extends MapOfListsExecutor<String, String> {
        protected Wrapper(MapOfLists<String, String> target) {
            super(target);
        }

        public static void validate(CellRangeAddress keyA, CellRangeAddress valueA) {
            MapOfListsExecutor.validate(keyA, valueA);
        }

    }

}
