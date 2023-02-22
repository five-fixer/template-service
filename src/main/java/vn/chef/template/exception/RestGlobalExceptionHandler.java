package vn.chef.template.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import vn.chef.common.exception.BaseGlobalExceptionHandler;
import vn.chef.common.web.handler.RestErrorResponse;

@Slf4j
@RestControllerAdvice
public class RestGlobalExceptionHandler extends BaseGlobalExceptionHandler {

    @ExceptionHandler({JpaSystemException.class, DataIntegrityViolationException.class})
    public ResponseEntity<RestErrorResponse> constraintViolationHandler(DataAccessException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(RestErrorResponse.of(HttpStatus.CONFLICT.value(), ex.getMessage(), this.getTraceId()));
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<RestErrorResponse> resourceAccessExceptionHandler(ResourceAccessException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE.value()).body(RestErrorResponse.of(HttpStatus.SERVICE_UNAVAILABLE.value(), ex.getMessage(), this.getTraceId()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestErrorResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(RestErrorResponse.of(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), this.getTraceId()));
    }

}
