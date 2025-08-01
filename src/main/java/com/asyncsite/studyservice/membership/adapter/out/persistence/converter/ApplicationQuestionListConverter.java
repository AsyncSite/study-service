package com.asyncsite.studyservice.membership.adapter.out.persistence.converter;

import com.asyncsite.studyservice.membership.domain.model.ApplicationQuestion;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.List;

@Converter
public class ApplicationQuestionListConverter implements AttributeConverter<List<ApplicationQuestion>, String> {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public String convertToDatabaseColumn(List<ApplicationQuestion> questions) {
        if (questions == null || questions.isEmpty()) {
            return "[]";
        }
        try {
            return objectMapper.writeValueAsString(questions);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert ApplicationQuestion list to JSON", e);
        }
    }
    
    @Override
    public List<ApplicationQuestion> convertToEntityAttribute(String json) {
        if (json == null || json.isEmpty() || "[]".equals(json)) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<List<ApplicationQuestion>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert JSON to ApplicationQuestion list", e);
        }
    }
}