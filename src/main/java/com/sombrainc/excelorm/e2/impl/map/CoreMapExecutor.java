package com.sombrainc.excelorm.e2.impl.map;

import com.sombrainc.excelorm.e2.impl.CoreActions;
import com.sombrainc.excelorm.e2.impl.CoreExecutor;
import org.apache.poi.ss.usermodel.Cell;

import java.util.Map;
import java.util.Optional;

public abstract class CoreMapExecutor<K, V> extends CoreExecutor<Map<K, V>> {

    protected CoreMapExecutor(CoreActions core) {
        super(core.getEReaderContext());
    }

    protected void validateOnPureObjects(MapHolder holder) {
        validateOnPureObject(holder.getKeyClass(), "Key object is not supported.");
        validateOnPureObject(holder.getValueClass(), "Value object is not supported.");
    }

    protected<K1, V1> boolean isUntilByKeyReached(MapHolder<K1, V1> holder, Cell keyCell) {
        return Optional.ofNullable(holder.getKeyUntil()).map(func -> func.apply(keyCell)).orElse(false);
    }

    protected<K1, V1> boolean filterByKey(MapHolder<K1, V1> holder, Cell keyCell) {
        return Optional.ofNullable(holder.getKeyFilter()).map(func -> !func.apply(keyCell)).orElse(false);
    }

}
