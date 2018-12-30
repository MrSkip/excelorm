package com.sombrainc.excelorm;

import com.sombrainc.excelorm.exception.POIRuntimeException;
import com.sombrainc.excelorm.implementor.CellIndexTracker;
import com.sombrainc.excelorm.implementor.CellTacticFactory;
import com.sombrainc.excelorm.utils.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.InputStream;
import java.lang.reflect.Field;

import static com.sombrainc.excelorm.utils.ReflectionUtils.getInstance;
import static com.sombrainc.excelorm.utils.ReflectionUtils.setFieldViaReflection;

public class Excelorm {

    /**
     * To load the the data into POJO object based on selected sheet
     *
     * @param sheet       selected Apache POI sheet
     * @param targetClass simple class
     * @param <E>         target POJO class
     * @return instance with loaded data
     */
    public static <E> E read(Sheet sheet, Class<E> targetClass) {
        CellIndexTracker tracker = new CellIndexTracker();
        return read(sheet, targetClass, tracker);
    }

    /**
     * To load the the data into POJO object based on selected sheet.
     * And with ability to track and iterate over collection types.
     *
     * @param sheet       selected Apache POI sheet
     * @param targetClass simple class
     * @param tracker     to keep track of index/strategy while iterating over collection
     * @param <E>         target POJO class
     * @return instance with loaded data
     */
    public static <E> E read(Sheet sheet, Class<E> targetClass, CellIndexTracker tracker) {
        if (sheet == null) {
            throw new RuntimeException("Sheet could not be null");
        }
        E instance = getInstance(targetClass);
        Field[] allFields = FieldUtils.getAllFields(targetClass);
        for (Field field : allFields) {
            new CellTacticFactory().getCellTactic(field, instance, sheet, tracker)
                    .ifPresent(handler -> setFieldViaReflection(instance, field, handler.process()));
        }
        return instance;
    }

    /**
     * To load the the data into POJO object based on excel doc and selected sheet name.
     *
     * @param docInputStream input stream to the excel file
     * @param sheetName      selected Apache POI sheet
     * @param targetClass    simple class
     * @param <E>            target POJO class
     * @return instance with loaded data
     */
    public static <E> E read(InputStream docInputStream, String sheetName, Class<E> targetClass) {
        if (docInputStream == null) {
            throw new NullPointerException("InputSteam could not be null");
        }
        if (StringUtils.isNullOrEmpty(sheetName)) {
            throw new NullPointerException("Sheet name could not be null");
        }
        try (Workbook wb = WorkbookFactory.create(docInputStream)) {
            Sheet sheet = wb.getSheet(sheetName);
            return read(sheet, targetClass);
        } catch (Exception e) {
            throw new POIRuntimeException(e);
        }
    }

}
