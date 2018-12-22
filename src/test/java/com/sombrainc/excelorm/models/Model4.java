package com.sombrainc.excelorm.models;

import com.sombrainc.excelorm.annotation.CellCollection;
import lombok.Data;

import java.util.List;

@Data
public class Model4 {

    @CellCollection(cells = "a7:d9")
    private List<String> list;

}
