package com.sombrainc.excelorm.e2.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Accessors(chain = true)
public class UserDTO {

    private String name;
    private String intAsStr;
    private List<String> listOfIntAsStr;
    private Set<String> setOfIntAsStr;
    private List<Integer> listOfInt;
    private Map<String, String> map;

    private String dontMapThisField;
    private Set dontMapThisField1;
    private List dontMapThisField2;
    private Map dontMapThisField3;

    private TestEnum testEnum;

    public enum TestEnum {
        T1,
        T2,
        T3
    }

}
