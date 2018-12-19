package com.sombrainc.excelorm;

import com.sombrainc.excelorm.implementor.ExcelReader;
import com.sombrainc.excelorm.model.TestDTO;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;

public class ExcelOrm {
    public static final String PATH = "/test.xlsx";

    public static void main(String[] args) throws IOException, InvalidFormatException {
        try (XSSFWorkbook wb = new XSSFWorkbook(new File(ExcelOrm.class.getResource(PATH).getFile()))) {
            TestDTO model = ExcelReader.read(wb.getSheet("Sheet1"), TestDTO.class);
            System.out.println(model);
//            model.getStudents().forEach((s, student) -> System.out.println(s + ": " + student));
        }
    }

}
