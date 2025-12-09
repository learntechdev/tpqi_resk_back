package com.TPQI.thai2learn.entities.tpqi_asm.types;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AssessmentStatusConverter implements AttributeConverter<AssessmentStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(AssessmentStatus status) {
        if (status == null) {
            return null;
        }
        return status.getCode();
    }

    @Override
    public AssessmentStatus convertToEntityAttribute(Integer code) {
        if (code == null) {
            return null;
        }
        return AssessmentStatus.of(code);
    }
}