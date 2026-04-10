package ru.reel.CollectionService.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.reel.CollectionService.service.issue.error.ErrorReason;
import ru.reel.CollectionService.service.issue.error.ParamRequestError;
import ru.reel.CollectionService.service.issue.error.RequestError;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<RequestError> handleJsonParseError(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest().body(RequestError
                .builder()
                .errorReason(ErrorReason.JSON_FORMAT)
                .message(e.getMessage())
                .build());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ParamRequestError> handleInvalidParamTypeError(MethodArgumentTypeMismatchException e) {
        return ResponseEntity.badRequest().body(ParamRequestError
                .builder()
                .param(e.getParameter().getParameterName())
                .errorReason(ErrorReason.BAD_DATA_TYPE)
                .message(e.getMessage())
                .build());
    }
}
