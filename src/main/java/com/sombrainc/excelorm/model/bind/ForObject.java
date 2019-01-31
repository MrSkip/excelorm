package com.sombrainc.excelorm.model.bind;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author <a href=dimon.mula@gmail.com>Dmytro Mula</a>
 * Date: 31.01.19
 */
public class ForObject<T> implements ExcelReader<T> {

    public ForObject<T> resolve(Consumer<String> confConsumer) {
        Supplier<String> stringSupplier;
        return this;
    }

    @Override
    public T read() {
        return null;
    }

}