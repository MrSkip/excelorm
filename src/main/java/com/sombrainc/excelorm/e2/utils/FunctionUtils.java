package com.sombrainc.excelorm.e2.utils;

import com.sombrainc.excelorm.e2.impl.BindField;

import java.util.Optional;
import java.util.function.Function;

public class FunctionUtils {

    private FunctionUtils() {
    }

    public static boolean untilFunction(Function<BindField, Boolean> until, BindField keyCell) {
        return Optional.ofNullable(until).map(func -> func.apply(keyCell)).orElse(false);
    }

    public static boolean filterFunction(Function<BindField, Boolean> until, BindField keyCell) {
        return !Optional.ofNullable(until).map(func -> func.apply(keyCell)).orElse(true);
    }

}
