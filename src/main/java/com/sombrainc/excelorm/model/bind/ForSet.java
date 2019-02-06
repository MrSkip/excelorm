package com.sombrainc.excelorm.model.bind;

import java.util.Set;

/**
 * @author <a href=dimon.mula@gmail.com>Dmytro Mula</a>
 * Date: 31.01.19
 */
public class ForSet<V> extends ForCollection<Set<V>, V> {

    public ForSet(Set<V> set) {
        super(set);
    }
}
