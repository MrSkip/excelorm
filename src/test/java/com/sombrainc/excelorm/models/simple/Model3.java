package com.sombrainc.excelorm.models.simple;

import com.sombrainc.excelorm.annotation.CellCollection;
import lombok.Data;

import java.util.List;

@Data
public class Model3 {

    @CellCollection(cells = "a7")
    private List<String> list;

}
