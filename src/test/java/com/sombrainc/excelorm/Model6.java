package com.sombrainc.excelorm;

import com.sombrainc.excelorm.annotation.Cell;
import com.sombrainc.excelorm.enumeration.DataQualifier;
import lombok.Data;

import java.util.List;

@Data
public class Model6 {

    @Cell(position = "a7", strategy = DataQualifier.ROW_UNTIL_NULL)
    private List<String> list;

}
