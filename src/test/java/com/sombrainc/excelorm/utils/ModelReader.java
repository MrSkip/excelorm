package com.sombrainc.excelorm.utils;

import com.sombrainc.excelorm.Excelorm;
import com.sombrainc.excelorm.e2.EReader;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModelReader {
    public static final String PATH = "/test.xlsx";

    public static <E> E getModel(Class<E> modelClass) {
        return getModel(modelClass, "position");
    }

    public static synchronized <E> E getModel(Class<E> modelClass, String sheetName) {
        try (XSSFWorkbook wb = new XSSFWorkbook(new File(ModelReader.class.getResource(PATH).getFile()))) {
            return Excelorm.read(wb.getSheet(sheetName), modelClass);
        } catch (InvalidFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized void executeForE2(String sheetName, Consumer<EReader> reader) {
        try (XSSFWorkbook wb = new XSSFWorkbook(new File(ModelReader.class.getResource(PATH).getFile()))) {
            EReader eReader = new EReader(wb.getSheet(sheetName));
            reader.accept(eReader);
        } catch (InvalidFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized <E> E getModel(InputStream doc, Class<E> modelClass, String sheetName) {
        return Excelorm.read(doc, sheetName, modelClass);
    }

}
