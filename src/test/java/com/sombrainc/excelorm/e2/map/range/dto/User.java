package com.sombrainc.excelorm.e2.map.range.dto;

import lombok.Data;

import java.util.List;

@Data
public class User {
    private String symbol;
    private Integer integer;
    private List<Integer> integers;
}
