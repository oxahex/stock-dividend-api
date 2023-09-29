package com.oxahex.stockdividendapi.exception;

import lombok.*;

@Data
@Builder
public class ErrorResponse {
    private int code;
    private String message;
}