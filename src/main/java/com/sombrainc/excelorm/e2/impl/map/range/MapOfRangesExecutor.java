package com.sombrainc.excelorm.e2.impl.map.range;

import com.sombrainc.excelorm.e2.impl.Bind;
import com.sombrainc.excelorm.e2.impl.BindField;
import com.sombrainc.excelorm.e2.impl.map.CoreMapExecutor;
import com.sombrainc.excelorm.e2.impl.map.MapHolder;
import com.sombrainc.excelorm.exception.IncorrectRangeException;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.sombrainc.excelorm.e2.utils.FunctionUtils.filterFunction;
import static com.sombrainc.excelorm.e2.utils.FunctionUtils.untilFunction;
import static com.sombrainc.excelorm.utils.ExcelUtils.*;
import static com.sombrainc.excelorm.utils.ExcelValidation.isOneCellSelected;

public class MapOfRangesExecutor<K, V> extends CoreMapExecutor<K, V> {
    private final MapHolder<K, V> holder;

    protected MapOfRangesExecutor(MapOfRanges<K, V> target) {
        super(target);
        this.holder = target.holder;
    }

    @Override
    public Map<K, V> execute() {
        validateOnPureObjects(holder);

        final CellRangeAddress keyRange = obtainRange(holder.getKeyRange());
        CellRangeAddress valueRange = obtainRange(holder.getValueRange());

        if (isOneCellSelected(valueRange)) {
            valueRange = makeTheSameLengthAndDirection(keyRange, valueRange);
        } else if (keyRange.getNumberOfCells() != valueRange.getNumberOfCells()) {
            throw new IncorrectRangeException("Selected ranges should have the same number of cells");
        }

        final List<Pair<Bind, CellRangeAddress>> bindOfPairs = getBinds(holder.getBinds());

        final Map<K, V> map = new HashMap<>();
        final Iterator<CellAddress> valueIterator = valueRange.iterator();
        final FormulaEvaluator evaluator = createFormulaEvaluator();

        int counter = -1;
        final Sheet sheet = loadSheet();
        for (CellAddress keyAddr : keyRange) {
            counter++;
            final Cell keyCell = getOrCreateCell(sheet, keyAddr);
            final BindField keyBindField = new BindField(keyCell, evaluator);

            if (untilFunction(holder.getKeyUntil(), keyBindField)) {
                break;
            }
            if (filterFunction(holder.getKeyFilter(), keyBindField)) {
                valueIterator.next();
                continue;
            }

            final K key = readRequestedType(
                    evaluator, keyBindField, holder.getKeyMapper(), holder.getKeyClass()
            );

            final Cell valueCell = getOrCreateCell(sheet, valueIterator.next());
            final V value = readAsPureOrCustomObject(
                    bindOfPairs, valueRange, evaluator, counter,
                    new BindField(valueCell, evaluator), holder.getValueMapper(), holder.getValueClass()
            );

            map.putIfAbsent(key, value);
        }

        return map;
    }
}
