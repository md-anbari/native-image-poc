package dev.anbari.native_image_poc.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Detail {
    private Double salary;
    private String contractType;
    private String year;
}