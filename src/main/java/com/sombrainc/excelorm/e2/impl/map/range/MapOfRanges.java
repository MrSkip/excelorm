package com.sombrainc.excelorm.e2.impl.map.range;

import com.sombrainc.excelorm.e2.impl.CoreActions;
import com.sombrainc.excelorm.e2.impl.CoreExecutor;
import com.sombrainc.excelorm.e2.impl.EReaderContext;
import com.sombrainc.excelorm.e2.impl.map.MapHolder;
import org.apache.poi.ss.usermodel.Cell;

import java.util.Map;
import java.util.function.Function;

public class MapOfRanges<K, V> extends CoreActions<Map<K, V>> {
    protected MapHolder<K,V> holder;

    public MapOfRanges(EReaderContext EReaderContext, Class<K> key, Class<V> value) {
        super(EReaderContext);
        holder = new MapHolder<>();
        holder.setKeyClass(key);
        holder.setValueClass(value);
    }

    public MapOfRanges(EReaderContext EReaderContext, MapHolder<K, V> holder) {
        super(EReaderContext);
        this.holder = holder;
    }

    @Override
    protected CoreExecutor<Map<K, V>> invokeExecutor() {
        return new MapOfRangesExecutor<>(this);
    }

    public MapOfRanges<K, V> pick(String key, String value) {
        holder.setKeyRange(key).setValueRange(value);
        return new MapOfRanges<>(getEReaderContext(), holder);
    }

    public MapOfRanges<K, V> map(Function<Cell, K> mapper) {
        holder.setKeyMapper(mapper);
        return new MapOfRanges<>(this.getEReaderContext(), holder);
    }

    public MapOfRanges<K, V> mapValue(Function<Cell, V> value) {
        holder.setValueMapper(value);
        return new MapOfRanges<>(this.getEReaderContext(), holder);
    }

    public MapOfRanges<K, V> filter(Function<Cell, Boolean> key) {
        holder.setKeyFilter(key);
        return new MapOfRanges<>(this.getEReaderContext(), holder);
    }

    public MapOfRanges<K, V> until(Function<Cell, Boolean> key) {
        holder.setKeyUntil(key);
        return new MapOfRanges<>(this.getEReaderContext(), holder);
    }

}
