package com.sombrainc.excelorm.e2.impl.map.list;

import com.sombrainc.excelorm.e2.impl.BindField;
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

    /**
     * Used to specify the locations of keys and values
     *
     * @param key   location of keys on the spreadsheet
     * @param value location of values on the spreadsheet
     * @return set of other functions
     */
    public MapOfLists<K, V> pick(String key, String value) {
        holder.setKeyRange(key).setValueRange(value);
        return new MapOfLists<>(getEReaderContext(), holder);
    }

    /**
     * Prevent increasing the range for value.
     * Only the explicitly selected values will be taken
     *
     * @return set of other functions
     */
    public MapOfLists<K, V> freeze() {
        holder.setFrozen(true);
        return new MapOfLists<>(getEReaderContext(), holder);
    }

    /**
     * Used to define a mapper for a key
     *
     * @param mapper mapper for a value
     * @return set of other functions
     */
    public MapOfLists<K, V> map(Function<BindField, K> mapper) {
        holder.setKeyMapper(mapper);
        return new MapOfLists<>(this.getEReaderContext(), holder);
    }

    /**
     * Used to define a mapper for value
     *
     * @param mapper mapper for a value
     * @return set of other functions
     */
    public MapOfLists<K, V> mapValue(Function<BindField, V> mapper) {
        holder.setValueMapper(mapper);
        return new MapOfLists<>(this.getEReaderContext(), holder);
    }

    /**
     * Define a filter for a key
     *
     * @param filter key filter function
     * @return set of other functions
     */
    public MapOfLists<K, V> filter(Function<BindField, Boolean> filter) {
        holder.setKeyFilter(filter);
        return new MapOfLists<>(this.getEReaderContext(), holder);
    }

    /**
     * Define a filter for a value
     *
     * @param filter value filter function
     * @return set of other functions
     */
    public MapOfLists<K, V> filterValue(Function<BindField, Boolean> filter) {
        holder.setValueFilter(filter);
        return new MapOfLists<>(this.getEReaderContext(), holder);
    }

    /**
     * Iterate over keys until some special condition
     *
     * @param until specified condition
     * @return set of other functions
     */
    public MapOfLists<K, V> until(Function<BindField, Boolean> until) {
        holder.setKeyUntil(until);
        return new MapOfLists<>(this.getEReaderContext(), holder);
    }

}
