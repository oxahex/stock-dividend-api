package com.oxahex.stockdividendapi.exception.impl;

import com.oxahex.stockdividendapi.exception.AbstractException;
import org.springframework.http.HttpStatus;

/**
 * status code - 400
 * error msg - 존재하지 않는 회사명 입니다.
 */
public class NoCompanyException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "존재하지 않는 회사명 입니다.";
    }
}