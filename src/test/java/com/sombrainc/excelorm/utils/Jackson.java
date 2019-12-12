package com.sombrainc.excelorm.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;

public class Jackson {

    private Jackson() {
    }

    public static <T> T parseTo(TypeReference<T> type, String resourcePath) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper.readValue(Jackson.class.getResourceAsStream(resourcePath), type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
