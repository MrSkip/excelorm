package com.sombrainc.excelorm;

import com.sombrainc.excelorm.model.bind.ForCollection;
import com.sombrainc.excelorm.model.bind.ForMap;
import com.sombrainc.excelorm.model.bind.ForObject;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

public class Excelorm2 {
    private Sheet sheet;

    public Excelorm2(Sheet sheet) {

    }

    public Excelorm2(InputStream inputStream, String sheetName) {

    }

    public Excelorm2(String path, String sheetName) {

    }

    public <T> ForObject<T> forObject(Class<T> reference) {
        return new ForObject<>(reference);
    }

    public <T, R> ForCollection<T> forCollection(Collection<T> collection) {
        return new ForCollection<>(collection);
    }

    public <K, V> ForMap<K, V> forMap(Map<K, V> map) {
        return new ForMap<>(map);
    }

    public <T, R> ForCollection<T> forCollection(T strings, Main.TypeReference<R> stringTypeReference) {
        return null;
    }
}
