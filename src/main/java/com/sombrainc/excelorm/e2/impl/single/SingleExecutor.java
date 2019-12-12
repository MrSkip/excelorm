package com.sombrainc.excelorm.e2.impl.single;

import com.sombrainc.excelorm.Excelorm;
import com.sombrainc.excelorm.e2.impl.Bind;
import com.sombrainc.excelorm.e2.impl.BindField;
import com.sombrainc.excelorm.e2.impl.MiddleExecutor;
import com.sombrainc.excelorm.exception.POIRuntimeException;
import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.sombrainc.excelorm.utils.ExcelUtils.*;
import static com.sombrainc.excelorm.utils.ExcelValidation.isOneCellSelected;
import static com.sombrainc.excelorm.utils.TypesUtils.isAmongDefinedTypes;

@Getter
public class SingleExecutor<T> extends MiddleExecutor<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SingleExecutor.class);

    private final SinglePick<T> target;

    public SingleExecutor(SinglePick<T> target) {
        super(target.getEReaderContext());
        this.target = target;
    }

    @Override
    public T execute() {
        if (!isAmongDefinedTypes(target.aClass)) {
            return processUndefinedTypes();
        }
        final Cell cell = obtainCell();
        if (target.mapper == null) {
            return readGenericValueFromSheet(target.aClass, cell, createFormulaEvaluator());
        }
        return target.mapper.apply(new BindField(cell, createFormulaEvaluator()));
    }

    private Cell obtainCell() {
        CellRangeAddress cellAddresses = obtainRange(target.cell);
        if (!isOneCellSelected(cellAddresses)) {
            LOGGER.info("Range is found. Only the first cell will be processed");
        }
        return getOrCreateCell(loadSheet(), cellAddresses.getFirstRow(), cellAddresses.getFirstColumn());
    }

    private T processUndefinedTypes() {
        if (target.binds.isEmpty()) {
            return processMapperOrAnnotation();
        }
        // custom user definition of the object
        List<Pair<Bind, CellRangeAddress>> bindOfPairs = target.binds.stream()
                .map(buildPair()).collect(Collectors.toList());
        return readForSingleObject(bindOfPairs, target.aClass, createFormulaEvaluator());
    }

    private T processMapperOrAnnotation() {
        if (target.mapper == null) {
            // assuming that annotations are on the object
            return Excelorm.read(loadSheet(), target.aClass);
        }
        LOGGER.info("Trying to parse the value from spreadsheet based on user entered mapper");
        final Cell cell = obtainCell();
        return target.mapper.apply(new BindField(cell, createFormulaEvaluator()));
    }

    private Function<Bind, Pair<Bind, CellRangeAddress>> buildPair() {
        return bind -> {
            if (bind == null) {
                throw new POIRuntimeException("Bind object could not be null");
            }
            return Pair.of(bind, obtainRange(bind.getInitialCell()));
        };
    }

}
