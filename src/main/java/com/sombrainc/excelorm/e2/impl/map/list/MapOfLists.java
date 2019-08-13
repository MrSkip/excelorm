package com.sombrainc.excelorm.e2.impl.map.list;

import com.sombrainc.excelorm.e2.impl.CoreActions;
import com.sombrainc.excelorm.e2.impl.CoreExecutor;
import com.sombrainc.excelorm.e2.impl.EReaderContext;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.poi.ss.usermodel.Cell;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class MapOfLists<K, V> extends CoreActions<Map<K, List<V>>> {
    protected Holder holder;

    public MapOfLists(EReaderContext EReaderContext, Class<K> key, Class<V> value) {
        super(EReaderContext);
        holder = new Holder();
        holder.setKeyClass(key);
        holder.setValueClass(value);
    }

    public MapOfLists(EReaderContext EReaderContext, Holder holder) {
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

    @Data
    @Accessors(chain = true)
    public class Holder {
        protected Class<K> keyClass;
        protected Class<V> valueClass;

        protected Function<Cell, K> keyMapper;
        protected Function<Cell, V> valueMapper;

        protected Function<Cell, Boolean> keyUntil;
        protected Function<Cell, Boolean> valueUntil;

        protected Function<Cell, Boolean> keyFilter;
        protected Function<Cell, Boolean> valueFilter;

        protected String keyRange;
        protected String valueRange;
    }

}
