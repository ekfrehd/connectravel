package com.connectravel.domain.dto.part;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class PartDto {
    private Long crewId;
    private String title;
    private String body;
}
