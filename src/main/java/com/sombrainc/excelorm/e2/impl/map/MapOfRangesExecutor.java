package com.sombrainc.excelorm.e2.impl.map;

import com.sombrainc.excelorm.e2.impl.CoreExecutor;
import com.sombrainc.excelorm.exception.IncorrectRangeException;
import com.sombrainc.excelorm.exception.TypeIsNotSupportedException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.sombrainc.excelorm.utils.ExcelUtils.*;
import static com.sombrainc.excelorm.utils.TypesUtils.isPureObject;

public class MapOfRangesExecutor<K, V> extends CoreExecutor<Map<K, V>> {
    private final MapOfRanges<K, V> target;

    protected MapOfRangesExecutor(MapOfRanges<K, V> range) {
        this.target = range;
    }

    @Override
    public Map<K, V> go() {
        if (!isPureObject(target.holder.keyClass)) {
            throw new TypeIsNotSupportedException("Key object is not supported. Please see the list of supported objects for this method.");
        }
        if (!isPureObject(target.holder.valueClass)) {
            throw new TypeIsNotSupportedException("Value object is not supported. Please see the list of supported objects for this method.");
        }

        final CellRangeAddress keyRange = obtainRange(target.holder.keyRange);
        final CellRangeAddress valueRange = obtainRange(target.holder.valueRange);

        if (keyRange.getNumberOfCells() != valueRange.getNumberOfCells()) {
            throw new IncorrectRangeException("Selected ranges should have the same number of cells");
        }

        final Map<K, V> map = new HashMap<>();
        final Iterator<CellAddress> valueIterator = valueRange.iterator();
        final FormulaEvaluator formulaEvaluator = target.getEReaderContext().getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();

        for (CellAddress keyAddr : keyRange) {
            final Cell keyCell = getOrCreateCell(target.getEReaderContext().getSheet(), keyAddr);
            if (Optional.ofNullable(target.holder.keyUntil).map(func -> func.apply(keyCell)).orElse(false)) {
                break;
            }
            if (!Optional.ofNullable(target.holder.keyFilter).map(func -> func.apply(keyCell)).orElse(true)) {
                valueIterator.next();
                continue;
            }
            final K key = readRequestedType(formulaEvaluator, keyCell, target.holder.keyMapper, target.holder.keyClass);
            final Cell valueCell = getOrCreateCell(target.getEReaderContext().getSheet(), valueIterator.next());
            final V value = readRequestedType(formulaEvaluator, valueCell, target.holder.valueMapper, target.holder.valueClass);
            map.putIfAbsent(key, value);
        }

        return map;
    }
}
