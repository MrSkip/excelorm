package com.sombrainc.excelorm.e2.impl.map.range;

import com.sombrainc.excelorm.e2.impl.*;
import com.sombrainc.excelorm.e2.impl.map.MapHolder;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class MapOfRanges<K, V> extends CoreActions<Map<K, V>> {
    protected MapHolder<K, V> holder;

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

    /**
     * Used to specify the locations of keys and values.
     * All the pairs of keys and values should be on the same row or column or have the same number of cells
     * <p>
     * For example, valid locations are (key/value):
     * - {A1:B4} / {C1} - in this example {C1} will be shifted based on key location and equals to C1:D4
     * - {A1} / {B1}
     * - {A1} / {A1}
     * - {A1:A2} / {A1:A2}
     * - {A1:A2} / {B1:B2}
     *
     * @param key   location of keys on the spreadsheet
     * @param value location of values on the spreadsheet
     * @return set of other functions
     */
    public MapOfRanges<K, V> pick(String key, String value) {
        holder.setKeyRange(key).setValueRange(value);
        return new MapOfRanges<>(getEReaderContext(), holder);
    }

    /**
     * Used to specify user object
     *
     * @param binds provided array of user objects to be mapped
     * @return set of other functions
     */
    public MapOfRanges<K, V> binds(Bind... binds) {
        this.holder.setBinds(Arrays.asList(Objects.requireNonNull(binds)));
        return new MapOfRanges<>(getEReaderContext(), holder);
    }

    /**
     * Used to define a mapper for a key
     *
     * @param mapper mapper for a key
     * @return set of other functions
     */
    public MapOfRanges<K, V> map(Function<BindField, K> mapper) {
        holder.setKeyMapper(mapper);
        return new MapOfRanges<>(this.getEReaderContext(), holder);
    }

    /**
     * Used to define a mapper for value
     *
     * @param mapper mapper for a value
     * @return set of other functions
     */
    public MapOfRanges<K, V> mapValue(Function<BindField, V> mapper) {
        holder.setValueMapper(mapper);
        return new MapOfRanges<>(this.getEReaderContext(), holder);
    }

    /**
     * Define a filter for a key
     *
     * @param filter key filter function
     * @return set of other functions
     */
    public MapOfRanges<K, V> filter(Function<BindField, Boolean> filter) {
        holder.setKeyFilter(filter);
        return new MapOfRanges<>(this.getEReaderContext(), holder);
    }

    /**
     * Iterate over keys until some special condition
     *
     * @param until specified condition
     * @return set of other functions
     */
    public MapOfRanges<K, V> until(Function<BindField, Boolean> until) {
        holder.setKeyUntil(until);
        return new MapOfRanges<>(this.getEReaderContext(), holder);
    }

}
