package com.sombrainc.excelorm.model.bind;

import com.sombrainc.excelorm.Main;

/**
 * @author <a href=dimon.mula@gmail.com>Dmytro Mula</a>
 * Date: 31.01.19
 */
public class ForMap<T> implements ExcelReader<T> {

    public ForMap(Main.TypeReference<T> reference) {

    }

    @Override
    public T read() {
        return null;
    }

}