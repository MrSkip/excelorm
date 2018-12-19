package com.sombrainc.excelorm;

import com.sombrainc.excelorm.implementor.ExcelReader;
import com.sombrainc.excelorm.model.TestDTO;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ModelContainer {
    private static final String PATH = "/test.xlsx";
    private static final Map<Class<?>, String> MODELS;

    private static Map<Class<?>, Object> examples;

    static {
        MODELS = Collections.unmodifiableMap(
                new HashMap<Class<?>, String>() {{
                    put(Model1.class, "position");
                    // add more
                }}
        );
        loadAllModels();
    }

    public static <E> E getModel(Class<E> modelClass) {
        return (E) examples.get(modelClass);
    }

    private static void loadAllModels() {
        try (XSSFWorkbook wb = new XSSFWorkbook(new File(ExcelOrm.class.getResource(PATH).getFile()))) {
            MODELS.forEach((modelClass, sheet) -> {
                Object example = ExcelReader.read(wb.getSheet(sheet), modelClass);
                examples.put(modelClass, example);
            });
        } catch (InvalidFormatException | IOException e) {
            e.printStackTrace();
        }
    }

}
