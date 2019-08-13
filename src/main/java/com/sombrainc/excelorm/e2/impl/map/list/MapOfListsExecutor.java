package com.sombrainc.excelorm.e2.impl.map.list;

import com.sombrainc.excelorm.e2.impl.CoreExecutor;
import com.sombrainc.excelorm.exception.IncorrectRangeException;
import com.sombrainc.excelorm.exception.TypeIsNotSupportedException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.*;

import static com.sombrainc.excelorm.utils.ExcelUtils.getOrCreateCell;
import static com.sombrainc.excelorm.utils.ExcelUtils.obtainRange;
import static com.sombrainc.excelorm.utils.TypesUtils.isPureObject;

public class MapOfListsExecutor<K, V> extends CoreExecutor<Map<K, List<V>>> {
    private final MapOfLists<K, V> target;

    protected MapOfListsExecutor(MapOfLists<K, V> target) {
        this.target = target;
    }

    @Override
    public Map<K, List<V>> go() {
        if (!isPureObject(target.holder.keyClass)) {
            throw new TypeIsNotSupportedException("Key object is not supported. Please see the list of supported objects for this method.");
        }
        if (!isPureObject(target.holder.valueClass)) {
            throw new TypeIsNotSupportedException("Value object is not supported. Please see the list of supported objects for this method.");
        }

        final CellRangeAddress keyRange = obtainRange(target.holder.keyRange);
        final CellRangeAddress valueRange = obtainRange(target.holder.valueRange);

        validate(keyRange, valueRange);

        final Map<K, List<V>> map = new HashMap<>();
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

            final List<V> values = readValue(valueIterator, formulaEvaluator, new ArrayList<>());

            map.putIfAbsent(key, values);
        }
        return map;
    }

    private List<V> readValue(Iterator<CellAddress> valueIterator, FormulaEvaluator formulaEvaluator, List<V> list) {
        while (valueIterator.hasNext()) {
            final CellAddress next = valueIterator.next();
            final Cell cell = getOrCreateCell(target.getEReaderContext().getSheet(), next);
            if (!Optional.ofNullable(target.holder.valueFilter).map(func -> func.apply(cell)).orElse(true)) {
                continue;
            }
            final V item = readRequestedType(formulaEvaluator, cell, target.holder.valueMapper, target.holder.valueClass);
            list.add(item);
        }
        return list;
    }

    private static void validate(CellRangeAddress keyA, CellRangeAddress valueA) {
        if (isOnOneDirection(keyA)) {
            final String message = "Cell range for value is not correct";
            if (isOnOneDirection(valueA)) {
                if (isRangesOnOneDirection(keyA, valueA) && keyA.getNumberOfCells() != valueA.getNumberOfCells()) {
                    throw new IncorrectRangeException(message);
                }
            } else {
                throw new IncorrectRangeException(message);
            }
        } else if (keyA.getNumberOfCells() != valueA.getNumberOfCells()) {
            throw new IncorrectRangeException("Cell range for key and cell range for value should have the same number of cells");
        }
    }

    private static boolean isOnOneDirection(CellRangeAddress addresses) {
        return addresses.getFirstRow() == addresses.getLastRow()
                || addresses.getLastColumn() == addresses.getLastColumn();
    }

    private static boolean isRangesOnOneDirection(CellRangeAddress keyA, CellRangeAddress valueA) {
        return (keyA.getFirstRow() == keyA.getLastRow() && valueA.getFirstRow() == valueA.getLastRow())
                || (keyA.getFirstColumn() == keyA.getLastColumn() && valueA.getFirstColumn() == valueA.getLastColumn());
    }

}
