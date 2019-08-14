package com.sombrainc.excelorm.e2.impl.map.range;

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
import java.util.Optional;

import static com.sombrainc.excelorm.utils.ExcelUtils.*;

public class MapOfRangesExecutor<K, V> extends CoreMapExecutor<K, V> {
    private final MapHolder<K,V> holder;

    protected MapOfRangesExecutor(MapOfRanges<K, V> target) {
        super(target);
        this.holder = target.holder;
    }

    @Override
    public Map<K, V> go() {
        validateOnPureObjects(holder);

        final CellRangeAddress keyRange = obtainRange(holder.getKeyRange());
        final CellRangeAddress valueRange = obtainRange(holder.getValueRange());

        if (keyRange.getNumberOfCells() != valueRange.getNumberOfCells()) {
            throw new IncorrectRangeException("Selected ranges should have the same number of cells");
        }

        final Map<K, V> map = new HashMap<>();
        final Iterator<CellAddress> valueIterator = valueRange.iterator();
        final FormulaEvaluator formulaEvaluator = createFormulaEvaluator();

        for (CellAddress keyAddr : keyRange) {
            final Cell keyCell = getOrCreateCell(getSheet(), keyAddr);
            if (isUntilByKeyReached(holder, keyCell)) {
                break;
            }
            if (filterByKey(holder, keyCell)) {
                valueIterator.next();
                continue;
            }
            final K key = readRequestedType(formulaEvaluator, keyCell, holder.getKeyMapper(), holder.getKeyClass());
            final Cell valueCell = getOrCreateCell(getSheet(), valueIterator.next());
            final V value = readRequestedType(formulaEvaluator, valueCell, holder.getValueMapper(), holder.getValueClass());
            map.putIfAbsent(key, value);
        }

        return map;
    }
}
