package com.oxahex.stockdividendapi.exception.impl;

import com.oxahex.stockdividendapi.exception.AbstractException;
import org.springframework.http.HttpStatus;

/**
 * status code - 400
 * error msg - 이미 존재하는 사용자명입니다.
 */
public class AlreadyExistUserException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "이미 존재하는 사용자명입니다.";
    }
}