package com.connectravel.domain.dto.dashboard;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
public class DashboardRequest {

    String strict;
    String sport;
}
