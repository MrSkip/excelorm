package com.sombrainc.excelorm.e2.impl;

import com.sombrainc.excelorm.exception.POIRuntimeException;
import com.sombrainc.excelorm.utils.StringUtils;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;

import static com.sombrainc.excelorm.utils.ExcelUtils.getOrCreateCell;

@Getter
public abstract class CoreExecutor<T> {
    protected final EReaderContext context;
    private Workbook workbook;

    protected CoreExecutor(EReaderContext context) {
        this.context = context;
    }

    protected T go() {
        try {
            return execute();
        } catch (RuntimeException ex) {
            throw new POIRuntimeException(ex);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    throw new POIRuntimeException(e);
                }
            }
        }
    }

    public abstract T execute();

    protected Sheet loadSheet() {
        if (context.sheet != null) {
            return context.sheet;
        }
        try {
            if (!StringUtils.isNullOrEmpty(context.path)) {
                workbook = new XSSFWorkbook(context.path);
            } else if (context.file != null) {
                workbook = new XSSFWorkbook(context.file);
            } else if (context.inputStream != null) {
                workbook = new XSSFWorkbook(context.inputStream);
            } else {
                throw new POIRuntimeException("Required properties are not set");
            }
            if (StringUtils.isNullOrEmpty(context.sheetName)) {
                return workbook.getSheetAt(1);
            } else {
                return workbook.getSheet(context.sheetName);
            }
        } catch (Exception e) {
            throw new POIRuntimeException(
                    "Something went wrong while loading excel document", e);
        }
    }

    protected Cell toCell(CellAddress address) {
        return getOrCreateCell(loadSheet(), address);
    }

    protected FormulaEvaluator createFormulaEvaluator() {
        return loadSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
    }
}
