package com.sombrainc.excelorm;

import com.sombrainc.excelorm.annotation.Cell;
import lombok.Data;

import java.util.List;

@Data
public class Model4 {

    @Cell(position = "a7:d9")
    private List<String> list;

}
