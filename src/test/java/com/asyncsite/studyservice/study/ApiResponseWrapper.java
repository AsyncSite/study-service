package com.asyncsite.studyservice.study;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ApiResponseWrapper<T>(
    boolean success,
    T data,
    Object error,
    String timestamp
) {
    public static <T> T extractData(String json, Class<T> dataClass, ObjectMapper objectMapper) {
        try {
            var jsonNode = objectMapper.readTree(json);
            var dataNode = jsonNode.get("data");
            return objectMapper.treeToValue(dataNode, dataClass);
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract data from API response", e);
        }
    }
}