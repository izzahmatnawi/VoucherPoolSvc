package com.vouchers.exception;

import com.vouchers.constant.Response;
import com.vouchers.model.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse> handleNotFoundException(final ApplicationException ex) {
        final ApiResponse response = new ApiResponse(Response.STATUS_FAILED, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse> handleUnauthorizedException(final ApplicationException ex) {
        final ApiResponse response = new ApiResponse(Response.STATUS_FAILED, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleAllOtherErrors(final Exception ex) {
        final ApiResponse response = new ApiResponse(Response.STATUS_FAILED, Response.ERROR_INTERNAL);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
