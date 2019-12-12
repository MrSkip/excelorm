package com.sombrainc.excelorm.e2.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;

@Data
@Accessors(chain = true)
public class DatesDTO {

    private LocalDate date;
    private List<LocalDate> dates;

}
