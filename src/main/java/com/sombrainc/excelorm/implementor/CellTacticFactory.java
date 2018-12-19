package com.sombrainc.excelorm.implementor;

import com.sombrainc.excelorm.annotation.Cell;
import com.sombrainc.excelorm.annotation.CellMap;
import com.sombrainc.excelorm.annotation.CellMark;
import com.sombrainc.excelorm.implementor.tactic.CellTypeHandler;
import com.sombrainc.excelorm.implementor.tactic.CellTypeMap;
import com.sombrainc.excelorm.implementor.tactic.CellTypeMark;
import com.sombrainc.excelorm.implementor.tactic.CellTypePosition;
import org.apache.poi.ss.usermodel.Sheet;

import java.lang.reflect.Field;
import java.util.Optional;

public class CellTacticFactory {

    public <E> Optional<CellTypeHandler> getCellTactic(Field field, E instance, Sheet sheet, CellIndexTracker tracker) {
        if (field.isAnnotationPresent(Cell.class)) {
            return Optional.of(new CellTypePosition<>(field, instance, sheet, tracker));
        } else if (field.isAnnotationPresent(CellMap.class)) {
            return Optional.of(new CellTypeMap<>(field, instance, sheet, tracker));
        } else if (field.isAnnotationPresent(CellMark.class)) {
            return Optional.of(new CellTypeMark<>(field, instance, sheet, tracker));
        }
        return Optional.empty();
    }

}
