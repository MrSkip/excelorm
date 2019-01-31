package com.sombrainc.excelorm.model.bind;

@FunctionalInterface
public interface ExcelReader<T> {

    T read();

}
