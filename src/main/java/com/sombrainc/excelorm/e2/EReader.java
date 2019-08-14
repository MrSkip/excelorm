package com.sombrainc.excelorm.e2;

import com.sombrainc.excelorm.e2.impl.EReaderContext;
import com.sombrainc.excelorm.e2.impl.list.ListOfRange;
import com.sombrainc.excelorm.e2.impl.map.range.MapOfRanges;
import com.sombrainc.excelorm.e2.impl.map.list.MapOfLists;
import com.sombrainc.excelorm.e2.impl.single.SinglePick;
import lombok.NonNull;
import org.apache.poi.ss.usermodel.Sheet;

public class EReader extends EReaderContext {

    public EReader(String path) {
        super.path = path;
    }

    @NonNull
    public EReader(Sheet sheet) {
        super.sheet = sheet;
    }

    @NonNull
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
