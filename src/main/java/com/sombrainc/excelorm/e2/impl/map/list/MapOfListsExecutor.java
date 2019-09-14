package com.sombrainc.excelorm.e2.impl.map.list;

import com.sombrainc.excelorm.e2.impl.BindField;
import com.sombrainc.excelorm.e2.impl.map.CoreMapExecutor;
import com.sombrainc.excelorm.e2.impl.map.MapHolder;
import com.sombrainc.excelorm.exception.IncorrectRangeException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.*;

import static com.sombrainc.excelorm.e2.utils.FunctionUtils.filterFunction;
import static com.sombrainc.excelorm.e2.utils.FunctionUtils.untilFunction;
import static com.sombrainc.excelorm.utils.ExcelUtils.getOrCreateCell;
import static com.sombrainc.excelorm.utils.ExcelUtils.obtainRange;

public class MapOfListsExecutor<K, V> extends CoreMapExecutor<K, List<V>> {
    private final MapHolder<K, V> holder;

    protected MapOfListsExecutor(MapOfLists<K, V> target) {
        super(target);
        this.holder = target.holder;
    }

    @Override
    public Map<K, List<V>> execute() {
        validateOnPureObjects(holder);

        final CellRangeAddress keyRange = obtainRange(holder.getKeyRange());
        final CellRangeAddress valueRange = obtainRange(holder.getValueRange());

        validate(keyRange, valueRange);

        final Map<K, List<V>> map = new HashMap<>();
        Iterator<CellAddress> valueIterator = valueRange.iterator();
        final FormulaEvaluator evaluator = createFormulaEvaluator();
        int counter = -1;

        for (CellAddress keyAddr : keyRange) {
            counter++;
            final BindField keyCell = new BindField(toCell(keyAddr), evaluator);
            if (untilFunction(holder.getKeyUntil(), keyCell)) {
                break;
            }
            if (filterFunction(holder.getKeyFilter(), keyCell)) {
                valueIterator = incrementValuesIterator(keyRange, valueRange, valueIterator, counter);
                continue;
            }
            final K key = readRequestedType(evaluator, keyCell, holder.getKeyMapper(), holder.getKeyClass());
            if (!isVector(keyRange) || !isVector(valueRange) || isSameVector(keyRange, valueRange)) {
                final V value = readRequestedType(evaluator, new BindField(toCell(valueIterator.next()), evaluator),
                        holder.getValueMapper(), holder.getValueClass());
                map.putIfAbsent(key, new ArrayList<V>() {{
                    add(value);
                }});
            } else {
                if (!map.isEmpty()) {
                    valueIterator = incrementValuesIterator(keyRange, valueRange, valueIterator, counter);
                }
                final List<V> values = readValue(valueIterator, evaluator, new ArrayList<>());
                map.putIfAbsent(key, values);
            }
        }
        return map;
    }

    private Iterator<CellAddress> incrementValuesIterator(CellRangeAddress keyRange, CellRangeAddress valueRange, Iterator<CellAddress> valueIterator, int counter) {
        if (isVector(keyRange) && !isHorizontal(keyRange, valueRange) && !isVertical(keyRange, valueRange)) {
            return adjustRangeBasedOnVector(valueRange, counter, keyRange).iterator();
        }
        valueIterator.next();
        return valueIterator;
    }

    private List<V> readValue(Iterator<CellAddress> valueIterator, FormulaEvaluator evaluator, List<V> list) {
        while (valueIterator.hasNext()) {
            final CellAddress next = valueIterator.next();
            final BindField cell = new BindField(toCell(next), evaluator);
            if (!Optional.ofNullable(holder.getValueFilter()).map(func -> func.apply(cell)).orElse(true)) {
                continue;
            }
            final V item = readRequestedType(evaluator, cell, holder.getValueMapper(), holder.getValueClass());
            list.add(item);
        }
        return list;
    }

    private static void validate(CellRangeAddress keyA, CellRangeAddress valueA) {
        if (isVector(keyA)) {
            final String message = "Cell range for value is not correct";
            if (isVector(valueA)) {
                if (isSameVector(keyA, valueA) && keyA.getNumberOfCells() != valueA.getNumberOfCells()) {
                    throw new IncorrectRangeException(message);
                }
            } else {
                throw new IncorrectRangeException(message);
            }
        } else if (keyA.getNumberOfCells() != valueA.getNumberOfCells()) {
            throw new IncorrectRangeException("Cell range for key and cell range for value should have the same number of cells");
        }
    }

    private static boolean isHorizontal(CellRangeAddress key, CellRangeAddress value) {
        return isHorizontal(key) && isHorizontal(value);
    }

    private static boolean isVertical(CellRangeAddress key, CellRangeAddress value) {
        return isVertical(key) && isVertical(value);
    }

    private static boolean isSameVector(CellRangeAddress keyA, CellRangeAddress valueA) {
        return (keyA.getFirstRow() == keyA.getLastRow() && valueA.getFirstRow() == valueA.getLastRow())
                || (keyA.getFirstColumn() == keyA.getLastColumn() && valueA.getFirstColumn() == valueA.getLastColumn());
    }

}
