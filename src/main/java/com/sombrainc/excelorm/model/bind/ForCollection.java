package com.sombrainc.excelorm.model.bind;

import com.sombrainc.excelorm.model.bind.decorator.ForCollectionDecorator;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * @author <a href=dimon.mula@gmail.com>Dmytro Mula</a>
 * Date: 31.01.19
 */
public class ForCollection<T> extends ExcelReader<T> {

    public ForCollection(Collection<T> t) {

    }

    public ForCollection<T> resolve(Consumer<ForCollectionDecorator<T>> conf) {
        return null;
    }

    @Override
    public T read() {
        return null;
    }

}
