package com.oxahex.stockdividendapi.exception.impl;

import com.oxahex.stockdividendapi.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class TickerNotFountException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "ticker가 올바르지 않습니다.";
    }
}