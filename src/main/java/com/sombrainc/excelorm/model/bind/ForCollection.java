package com.sombrainc.excelorm.model.bind;

import com.sombrainc.excelorm.Main;
import com.sombrainc.excelorm.model.bind.decorator.ForCollectionDecorator;

import java.util.function.Consumer;

/**
 * @author <a href=dimon.mula@gmail.com>Dmytro Mula</a>
 * Date: 31.01.19
 */
public class ForCollection<T, V> extends ExcelReader<T> {

    public ForCollection(T t, Main.TypeRef<V> v) {

    }

    public ForCollection(T t) {
    }

    public ForCollection<T, V> resolve(Consumer<ForCollectionDecorator<V>> conf) {
        return null;
    }

    @Override
    public T read() {
        return null;
    }

    @Override
    protected V mapper() {
        return null;
    }

}
