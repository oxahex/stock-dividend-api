package com.oxahex.stockdividendapi.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    // 필터와 비슷하게 컨트롤러 코드보다 좀 더 바깥쪽에서 동작하는 레이어
    // 필터보다는 가까움. 서비스에서 지정된 에러가 발생하면 Response로 잡아서 던질 수 있음

    @ExceptionHandler(AbstractException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(AbstractException e) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                                        .code(e.getStatusCode())
                                        .message(e.getMessage()).build();

        return new ResponseEntity<>(errorResponse, HttpStatus.resolve(e.getStatusCode()));
    }

}