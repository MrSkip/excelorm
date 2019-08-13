package com.sombrainc.excelorm.model.pojo;

import com.sombrainc.excelorm.exception.IncorrectRangeException;
import com.sombrainc.excelorm.utils.ExcelValidation;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Field;

public class CellDirection {
    private CellRangeAddress range;
    private Field field;

    public CellDirection(CellRangeAddress range, Field field) {
        this.range = range;
        this.field = field;
    }

    public CellRangeAddress straightRange() {
        return range;
    }

    public CellRangeAddress validateAndGetRange() {
        if (ExcelValidation.isOneCellSelected(range)) {
            return range;
        }
        if (!ExcelValidation.isIteratingOverColumns(range) && !ExcelValidation.isIteratingOverRows(range)) {
            throw new IncorrectRangeException(
                    String.format("This field '%s' supports iteration only over columns or rows." +
                            " It means each cell from the selected range must be on the same row or column", field.getName())
            );
        }
        return range;
    }
}
