package com.sombrainc.excelorm.e2.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class UserDTO {

    private String name;
    private String intAsStr;
    private List<String> listOfIntAsStr;

}
