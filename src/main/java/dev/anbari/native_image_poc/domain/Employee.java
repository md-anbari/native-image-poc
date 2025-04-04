package dev.anbari.native_image_poc.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Employee {
    private Long id;
    private String name;
    private String position;
    private List<String> details;
}