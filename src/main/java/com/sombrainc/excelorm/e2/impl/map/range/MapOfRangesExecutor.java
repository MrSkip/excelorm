package com.sombrainc.excelorm.e2.impl.map.range;

import com.sombrainc.excelorm.e2.impl.BindField;
import com.sombrainc.excelorm.e2.impl.map.CoreMapExecutor;
import com.sombrainc.excelorm.e2.impl.map.MapHolder;
import com.sombrainc.excelorm.exception.IncorrectRangeException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.sombrainc.excelorm.utils.ExcelUtils.*;

public class MapOfRangesExecutor<K, V> extends CoreMapExecutor<K, V> {
    private final MapHolder<K,V> holder;

    protected MapOfRangesExecutor(MapOfRanges<K, V> target) {
        super(target);
        this.holder = target.holder;
    }

    @Override
    public Map<K, V> execute() {
        validateOnPureObjects(holder);

        final CellRangeAddress keyRange = obtainRange(holder.getKeyRange());
        final CellRangeAddress valueRange = obtainRange(holder.getValueRange());

        if (keyRange.getNumberOfCells() != valueRange.getNumberOfCells()) {
            throw new IncorrectRangeException("Selected ranges should have the same number of cells");
        }

        final Map<K, V> map = new HashMap<>();
        final Iterator<CellAddress> valueIterator = valueRange.iterator();
        final FormulaEvaluator evaluator = createFormulaEvaluator();

        for (CellAddress keyAddr : keyRange) {
            final Cell keyCell = getOrCreateCell(loadSheet(), keyAddr);
            final BindField keyBindField = new BindField(keyCell, evaluator);
            if (isUntilByKeyReached(holder, keyBindField)) {
                break;
            }
            if (filterByKey(holder, keyBindField)) {
                valueIterator.next();
                continue;
            }
            final K key = readRequestedType(evaluator, keyBindField,
                    holder.getKeyMapper(), holder.getKeyClass());
            final Cell valueCell = getOrCreateCell(loadSheet(), valueIterator.next());
            final V value = readRequestedType(evaluator, new BindField(valueCell, evaluator),
                    holder.getValueMapper(), holder.getValueClass());
            map.putIfAbsent(key, value);
        }

        return map;
    }
}
