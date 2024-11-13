package com.kpi.routetracker.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
@RequiredArgsConstructor
public class RequestControllerAdvice {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ProblemDetail> handleBindException(BindException exception) {
        ProblemDetail problemDetail = ProblemDetail.
                forStatusAndDetail(HttpStatus.BAD_REQUEST, "Required fields are empty or null");

        problemDetail.setProperty("errors", exception.getAllErrors().stream().map(ObjectError::getDefaultMessage).toList());
        problemDetail.setTitle("Illegal argument");
        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ProblemDetail> handleNoSuchElementException(NoSuchElementException exception) {
        ProblemDetail problemDetail = ProblemDetail.
                forStatusAndDetail(HttpStatus.NOT_FOUND, "Not found");

        problemDetail.setProperty("errors", exception.getMessage());
        problemDetail.setTitle("Not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArgumentException(IllegalArgumentException exception) {
        ProblemDetail problemDetail = ProblemDetail.
                forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, "Unprocessable entity");

        problemDetail.setProperty("errors", exception.getMessage());
        problemDetail.setTitle("Unprocessable entity");
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(problemDetail);
    }

}
