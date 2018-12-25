package com.sombrainc.excelorm.utils;

import com.sombrainc.excelorm.ExcelOrm;
import com.sombrainc.excelorm.ExcelReader;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;

public class ModelReader {
    private static final String PATH = "/test.xlsx";

    public static <E> E getModel(Class<E> modelClass) {
        return getModel(modelClass, "position");
    }

    public static <E> E getModel(Class<E> modelClass, String sheetName) {
        try (XSSFWorkbook wb = new XSSFWorkbook(new File(ExcelOrm.class.getResource(PATH).getFile()))) {
            return ExcelReader.read(wb.getSheet(sheetName), modelClass);
        } catch (InvalidFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
