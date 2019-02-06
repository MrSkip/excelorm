package com.sombrainc.excelorm.model.bind;

import java.util.List;

/**
 * @author <a href=dimon.mula@gmail.com>Dmytro Mula</a>
 * Date: 31.01.19
 */
public class ForList<V> extends ForCollection<List<V>, V> {

    public ForList(List<V> list) {
        super(list);
    }
}
