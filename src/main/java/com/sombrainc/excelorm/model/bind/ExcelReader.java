package com.sombrainc.excelorm.model.bind;

//@FunctionalInterface
public abstract class ExcelReader<T> {

    protected abstract T read();

}
