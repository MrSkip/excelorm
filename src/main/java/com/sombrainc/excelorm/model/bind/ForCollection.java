package com.sombrainc.excelorm.model.bind;

import com.sombrainc.excelorm.Main;

/**
 * @author <a href=dimon.mula@gmail.com>Dmytro Mula</a>
 * Date: 31.01.19
 */
public class ForCollection<T> implements ExcelReader<T> {

    public ForCollection(Main.TypeReference<Iterable<T>> reference) {

    }

    public ForCollection() {

    }

    @Override
    public T read() {
        return null;
    }

}
