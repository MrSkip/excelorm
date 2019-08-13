package com.sombrainc.excelorm.e2.impl.map;

import com.sombrainc.excelorm.e2.impl.CoreActions;
import com.sombrainc.excelorm.e2.impl.CoreExecutor;
import com.sombrainc.excelorm.e2.impl.EReaderContext;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.poi.ss.usermodel.Cell;

import java.util.Map;
import java.util.function.Function;

public class MapOfRanges<K, V> extends CoreActions<Map<K, V>> {
    protected Holder holder;

    public MapOfRanges(EReaderContext EReaderContext, Class<K> key, Class<V> value) {
        super(EReaderContext);
        holder = new Holder();
        holder.setKeyClass(key);
        holder.setValueClass(value);
    }

    public MapOfRanges(EReaderContext EReaderContext, Holder holder) {
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

    @Data
    @Accessors(chain = true)
    public class Holder {
        protected Class<K> keyClass;
        protected Class<V> valueClass;

        protected Function<Cell, K> keyMapper;
        protected Function<Cell, V> valueMapper;

        protected Function<Cell, Boolean> keyUntil;

        protected Function<Cell, Boolean> keyFilter;

        protected String keyRange;
        protected String valueRange;
    }

}
