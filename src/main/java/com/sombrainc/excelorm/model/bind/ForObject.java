package com.sombrainc.excelorm.model.bind;

import com.sombrainc.excelorm.Main;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author <a href=dimon.mula@gmail.com>Dmytro Mula</a>
 * Date: 31.01.19
 */
public class ForObject<T> implements ExcelReader<T> {

    public ForObject(Main.TypeReference<T> reference) {

    }

    public ForObject(T reference) {

    }

    public ForObject(Class<T> reference) {

    }

    public ForObject<T> resolve(Consumer<ForObjectDecorator<T>> confConsumer) {
//        Supplier<String> stringSupplier;
        return this;
    }

    @Override
    public T read() {
        return null;
    }

}