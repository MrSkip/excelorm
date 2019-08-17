package com.sombrainc.excelorm.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class Jackson {

    private Jackson() {
    }

    public static <T> T parseTo(TypeReference<T> type, String resourcePath) {
        try {
            return new ObjectMapper().readValue(Jackson.class.getResourceAsStream(resourcePath), type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
