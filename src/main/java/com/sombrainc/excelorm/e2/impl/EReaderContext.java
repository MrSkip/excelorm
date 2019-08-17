package com.sombrainc.excelorm.e2.impl;

import lombok.Getter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.InputStream;

@Getter
public abstract class EReaderContext {
    protected String path;
    protected File file;
    protected InputStream inputStream;
    protected String sheetName;

    protected Sheet sheet;
}
