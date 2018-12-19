package com.sombrainc.excelorm;

import com.sombrainc.excelorm.annotation.Cell;
import lombok.Data;

import java.util.List;

@Data
public class Model3 {

    @Cell(position = "a7")
    private List<String> list;

}
