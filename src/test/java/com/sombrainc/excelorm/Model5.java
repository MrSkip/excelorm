package com.sombrainc.excelorm;

import com.sombrainc.excelorm.annotation.Cell;
import com.sombrainc.excelorm.enumeration.DataQualifier;
import lombok.Data;

import java.util.List;

@Data
public class Model5 {

    @Cell(position = "a7", strategy = DataQualifier.COLUMN_UNTIL_NULL)
    private List<String> list;

}
