package com.sombrainc.excelorm.model.bind;

import com.sombrainc.excelorm.model.bind.decorator.BasicDecorator;
import com.sombrainc.excelorm.model.bind.decorator.ForMapKeyDecorator;
import com.sombrainc.excelorm.model.bind.decorator.ForMapValueDecorator;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author <a href=dimon.mula@gmail.com>Dmytro Mula</a>
 * Date: 31.01.19
 */
public class ForMap<K, V> extends ExcelReader<Map<K, V>> {

    public ForMap(Map<K, V> map) {

    }

    public ForMap(Map<K, V> stringStringHashMap, HashMap<String, String> stringStringHashMap1) {

    }

    public ForMap<K, V> resolveKey(Consumer<ForMapKeyDecorator<K>> conf) {
        return this;
    }

    public ForMap<K, V> resolveValue(BiConsumer<ForMapValueDecorator<V>, BasicDecorator<K>> conf) {
        return this;
    }

    @Override
    protected Map<K, V> read() {
        return null;
    }

}