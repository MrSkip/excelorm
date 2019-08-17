package com.sombrainc.excelorm.e2;

import com.sombrainc.excelorm.e2.impl.EReaderContext;
import com.sombrainc.excelorm.e2.impl.list.ListOfRange;
import com.sombrainc.excelorm.e2.impl.map.range.MapOfRanges;
import com.sombrainc.excelorm.e2.impl.map.list.MapOfLists;
import com.sombrainc.excelorm.e2.impl.single.SinglePick;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.InputStream;

public class EReader extends EReaderContext {

    /**
     * Load specific {@code sheetName} sheet from the excel doc by path {@code path}
     *
     * @param path      path to the excel document
     * @param sheetName the name of sheet to load
     */
    public EReader(String path, String sheetName) {
        super.path = path;
        super.sheetName = sheetName;
    }

    /**
     * Load first sheet from the excel doc by path {@code path}
     *
     * @param path path to the excel document
     */
    public EReader(String path) {
        super.path = path;
    }

    /**
     * Load first sheet from the excel doc {@code inputStream}
     *
     * @param inputStream input stream to the excel document
     */
    public EReader(InputStream inputStream) {
        super.inputStream = inputStream;
    }

    /**
     * Load specific {@code sheetName} sheet from the excel doc {@code inputStream}
     *
     * @param inputStream input stream to the excel document
     * @param sheetName   the name of sheet to load
     */
    public EReader(InputStream inputStream, String sheetName) {
        super.inputStream = inputStream;
        super.sheetName = sheetName;
    }

    /**
     * Load first sheet from the excel doc {@code file}
     *
     * @param file excel document
     */
    public EReader(File file) {
        super.file = file;
    }

    /**
     * Load specific {@code sheetName} sheet from the excel doc {@code file}
     *
     * @param file      excel document
     * @param sheetName the name of sheet to load
     */
    public EReader(File file, String sheetName) {
        super.file = file;
        super.sheetName = sheetName;
    }

    /**
     * Set the sheet to process
     *
     * @param sheet excel sheet
     */
    public EReader(Sheet sheet) {
        super.sheet = sheet;
    }

    /**
     * Create the single object based on {@code aClass}
     *
     * @param aClass type
     * @param <T>    required object to be created
     * @return required object
     */
    public <T> SinglePick<T> single(Class<T> aClass) {
        return new SinglePick<>(this, aClass);
    }

    public <E> ListOfRange<E> listOf(Class<E> aClass) {
        return new ListOfRange<>(this, aClass);
    }

    public <K, V> MapOfRanges<K, V> mapOf(Class<K> key, Class<V> value) {
        return new MapOfRanges<>(this, key, value);
    }

    public <K, V> MapOfLists<K, V> mapOfList(Class<K> key, Class<V> value) {
        return new MapOfLists<>(this, key, value);
    }
}
