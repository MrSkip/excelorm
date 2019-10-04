package com.sombrainc.excelorm.e2.impl;

import com.sombrainc.excelorm.exception.POIRuntimeException;
import com.sombrainc.excelorm.utils.StringUtils;
import lombok.Getter;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
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
            if (ex instanceof POIRuntimeException) {
                throw ex;
            }
            throw new POIRuntimeException(ex);
        } finally {
            if (this.workbook != null) {
                try {
                    this.workbook.close();
                } catch (IOException e) {
                    throw new POIRuntimeException(e);
                }
            }
        }
    }

    public abstract T execute();

    protected Sheet loadSheet() {
        if (this.context.sheet != null) {
            return this.context.sheet;
        }
        try {
            this.workbook = loadWorkBook();
            this.context.sheet = defineSheet();
            return defineSheet();
        } catch (Exception e) {
            throw new POIRuntimeException(
                    "Something went wrong while loading excel document", e);
        }
    }

    private Sheet defineSheet() {
        if (StringUtils.isNullOrEmpty(this.context.sheetName)) {
            return this.workbook.getSheetAt(1);
        }
        return this.workbook.getSheet(this.context.sheetName);
    }

    private XSSFWorkbook loadWorkBook() throws IOException, InvalidFormatException {
        if (!StringUtils.isNullOrEmpty(this.context.path)) {
            return new XSSFWorkbook(this.context.path);
        } else if (this.context.file != null) {
            return new XSSFWorkbook(this.context.file);
        } else if (this.context.inputStream != null) {
            return new XSSFWorkbook(this.context.inputStream);
        } else {
            throw new POIRuntimeException("Required properties are not set");
        }
    }

    protected Cell toCell(CellAddress address) {
        return getOrCreateCell(loadSheet(), address);
    }

    protected FormulaEvaluator createFormulaEvaluator() {
        return loadSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
    }
}
