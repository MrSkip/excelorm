package com.sombrainc.excelorm.e2.impl;

import lombok.Getter;
import org.apache.poi.ss.usermodel.Sheet;

@Getter
public abstract class EReaderContext {
    protected String path;
    protected Sheet sheet;
}
