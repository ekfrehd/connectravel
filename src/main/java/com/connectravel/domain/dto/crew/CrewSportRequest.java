package com.connectravel.domain.dto.crew;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class CrewSportRequest {

    private String strict;
    private List<String> sportsList;
    private boolean loginStatus;
}
