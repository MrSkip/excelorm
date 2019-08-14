package com.sombrainc.excelorm.e2.impl.map.list;

import com.sombrainc.excelorm.e2.impl.CoreActions;
import com.sombrainc.excelorm.e2.impl.CoreExecutor;
import com.sombrainc.excelorm.e2.impl.EReaderContext;
import com.sombrainc.excelorm.e2.impl.map.MapHolder;
import org.apache.poi.ss.usermodel.Cell;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class MapOfLists<K, V> extends CoreActions<Map<K, List<V>>> {
    protected MapHolder<K, V> holder;

    public MapOfLists(EReaderContext EReaderContext, Class<K> key, Class<V> value) {
        super(EReaderContext);
        holder = new MapHolder<>();
        holder.setKeyClass(key);
        holder.setValueClass(value);
    }

    public MapOfLists(EReaderContext EReaderContext, MapHolder<K, V> holder) {
        super(EReaderContext);
        this.holder = holder;
    }

    @Override
    protected CoreExecutor<Map<K, List<V>>> invokeExecutor() {
        return new MapOfListsExecutor<>(this);
    }

    public MapOfLists<K, V> pick(String key, String value) {
        holder.setKeyRange(key).setValueRange(value);
        return new MapOfLists<>(getEReaderContext(), holder);
    }

    public MapOfLists<K, V> map(Function<Cell, K> mapper) {
        holder.setKeyMapper(mapper);
        return new MapOfLists<>(this.getEReaderContext(), holder);
    }

    public MapOfLists<K, V> mapValue(Function<Cell, V> value) {
        holder.setValueMapper(value);
        return new MapOfLists<>(this.getEReaderContext(), holder);
    }

    public MapOfLists<K, V> filter(Function<Cell, Boolean> key) {
        holder.setKeyFilter(key);
        return new MapOfLists<>(this.getEReaderContext(), holder);
    }

    public MapOfLists<K, V> filterValue(Function<Cell, Boolean> value) {
        holder.setValueFilter(value);
        return new MapOfLists<>(this.getEReaderContext(), holder);
    }

    public MapOfLists<K, V> until(Function<Cell, Boolean> key) {
        holder.setKeyUntil(key);
        return new MapOfLists<>(this.getEReaderContext(), holder);
    }

}
