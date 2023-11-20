package com.connectravel.domain.dto;

import com.connectravel.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorResponse {
    private final ErrorCode errorCode;
    private final String message;
}
