package dev.anbari.native_image_poc.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.anbari.native_image_poc.domain.DetailEntity;
import dev.anbari.native_image_poc.domain.Employee;
import dev.anbari.native_image_poc.domain.EmployeeEntity;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@UtilityClass
@Slf4j
public class EmployeeMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static EmployeeEntity toEntity(Employee dto) {
        List<DetailEntity> details = null;
        if (dto.getDetails() != null) {
            details = dto.getDetails().stream()
                    .map(detailJson -> {
                        try {
                            return objectMapper.readValue(detailJson, DetailEntity.class);
                        } catch (JsonProcessingException e) {
                            log.error("Failed to parse detail JSON: {}", e.getMessage(), e);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .toList();
        }

        return EmployeeEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .position(dto.getPosition())
                .details(details != null ? details : Collections.emptyList())
                .build();
    }

    public static Employee toDto(EmployeeEntity entity) {
        List<String> details = null;
        if (entity.getDetails() != null) {
            details = entity.getDetails().stream()
                    .map(detail -> {
                        try {
                            return objectMapper.writeValueAsString(detail);
                        } catch (JsonProcessingException e) {
                            log.error("Failed to serialize detail: {}", e.getMessage(), e);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        return Employee.builder()
                .id(entity.getId())
                .name(entity.getName())
                .position(entity.getPosition())
                .details(details != null ? details : Collections.emptyList())
                .build();
    }
}